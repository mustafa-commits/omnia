package com.sc.demo.service.announcements;

import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.announcements.AnnouncementsAttachment;
import com.sc.demo.model.announcements.AnnouncementsDetails;
import com.sc.demo.model.dto.announcements.AllAnnouncementsFamilyRequest;
import com.sc.demo.model.dto.announcements.AnnouncementsRequest;
import com.sc.demo.model.dto.announcements.PhoneAnnouncementsRequest;
import com.sc.demo.model.dto.announcements.AnnouncementsTokenRequest;
import com.sc.demo.model.notification.SendingType;
import com.sc.demo.model.users.Token;
import com.sc.demo.repository.announcements.AnnouncementsAttachmentRepo;
import com.sc.demo.repository.announcements.AnnouncementsDetailsRepo;
import com.sc.demo.repository.announcements.AnnouncementsRepo;
import com.sc.demo.repository.announcements.AnnouncementsTokenRepo;
import com.sc.demo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementsService {

    @Autowired
    private AnnouncementsRepo announcementsRepo;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private AnnouncementsDetailsRepo announcementsDetailsRepo;

    @Autowired
    private AnnouncementsAttachmentRepo announcementsAttachmentRepo;

    @Autowired
    private Environment environment;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AnnouncementsTokenRepo announcementsTokenRepo;

    // انشاء تبليغ
    public Announcements createAnnouncements(AnnouncementsRequest announcementsRequest,
                                             MultipartFile file, List<Long> userId, String token) {
        var userTokenId = tokenService.decodeToken(token.substring(7)).getSubject();
        Announcements announcements = new Announcements(announcementsRequest.title(),
                announcementsRequest.description(), announcementsRequest.sendingType() == SendingType.BRANCH ? announcementsRequest.branches() : null,
                announcementsRequest.sendingType(), announcementsRequest.createBy());

        System.out.println(announcements);
        announcements = announcementsRepo.save(announcements);

        Long createBy = Long.valueOf(userTokenId);

        if (announcementsRequest.sendingType() == SendingType.PRIVATE) {
            for (Long a : userId) {
                announcementsDetailsRepo.save(new AnnouncementsDetails(a, createBy, announcements));
            }
        }else if (announcementsRequest.sendingType() == SendingType.BRANCH) {
            String getUsersInBranch = announcementsRequest.branches();
            List<AnnouncementsTokenRequest> gettoken = jdbcClient.sql("""
                            SELECT T.TOKEN AS token
                                  ,U.USERID AS userId
                            FROM MOBAPP.SC_APP_USERS U
                            LEFT JOIN MOBAPP.SC_TOKEN T on (U.USERID = T.USER_ID)
                            WHERE U.BRANCHES = :branch
                    """)
                    .param("branch", getUsersInBranch)
                    .query(AnnouncementsTokenRequest.class)
                    .list();
            for (Long b : userId ) {
                announcementsDetailsRepo.save(new AnnouncementsDetails(b, createBy, announcements));
            }
        }

        if (file != null) try {
            String originalFilename = file.getOriginalFilename();
            String newFilename = System.nanoTime() + originalFilename.substring(originalFilename.lastIndexOf("."));
            String filePath = environment.getProperty("ATTACHMENT_PATH_ANNOUNCEMENTS") + newFilename;
            announcementsAttachmentRepo.save(new AnnouncementsAttachment(newFilename, createBy, announcements));
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return announcements;
    }

    // حفظ Token القادم من firebase في قاعدة البيانات
    public long saveToken(AnnouncementsTokenRequest announcementsTokenRequest) {
        Optional<Token> byToken = announcementsTokenRepo.findById(announcementsTokenRequest.userId());

        if (byToken.isPresent()) {
            Token token = byToken.get();
            token.setLastUpdate(LocalDateTime.now());
            token.setToken(announcementsTokenRequest.token());
            return announcementsTokenRepo.save(token).getUserId();
        } else {
            Token token = new Token();
            token.setToken(announcementsTokenRequest.token());
            token.setUserId(announcementsTokenRequest.userId());
            return announcementsTokenRepo.save(token).getUserId();
        }
    }

    // اشعارات التطيق لكل يززر
    public List<PhoneAnnouncementsRequest> PhoneAnnouncements(String token) {
        var userTokenId = tokenService.decodeToken(token.substring(7)).getSubject();

        return jdbcClient.sql("""
                   SELECT A.CREATE_DATE AS createDate
                          ,A.TITLE
                          ,A.DESCRIPTION
                          ,TO_CHAR(:path) || AA.FILE_NAME AS fileName
                   FROM SC_ANNOUNCEMENTS A
                   LEFT JOIN SC_ANNOUNCEMENTS_DETAILS AD ON A.ANNOUNCEMENTS_ID = AD.ANNOUNCEMENTS_ID
                   LEFT JOIN SC_ANNOUNCEMENTS_ATTACHMENT AA ON A.ANNOUNCEMENTS_ID = AA.ANNOUNCEMENTS_ID
                   WHERE AD.USER_ID = :userId OR A.SENDING_TYPE = 0
                """)
                .param("userId", userTokenId)
                .param("path", "http://37.239.42.53:1801/socialCare/V1/api/sc/photoAnnouncements/")
                .query(PhoneAnnouncementsRequest.class)
                .list();

    }

    // في الداشبورد جميع الاشعارات التي تصل للعائلة اذا كانت خاصة او عامة
    public List<AllAnnouncementsFamilyRequest> AllAnnouncementsFamily() {
        return jdbcClient.sql("""
                   SELECT A.ANNOUNCEMENTS_ID AS announcementsId
                         ,A.CREATE_DATE AS createDate
                         ,A.TITLE
                         ,TO_CHAR(:path) || AA.FILE_NAME AS fileName
                         ,A.DESCRIPTION
                         ,A.SENDING_TYPE AS sendingType
                   FROM SC_ANNOUNCEMENTS A
                   LEFT JOIN SC_ANNOUNCEMENTS_DETAILS AD ON A.ANNOUNCEMENTS_ID = AD.ANNOUNCEMENTS_ID
                   LEFT JOIN SC_ANNOUNCEMENTS_ATTACHMENT AA ON A.ANNOUNCEMENTS_ID = AA.ANNOUNCEMENTS_ID
                """)
                .param("path", "http://37.239.42.53:1801/socialCare/V1/api/sc/allAnnouncementsPhotos/")
                .query(AllAnnouncementsFamilyRequest.class)
                .list();

    }

    // حذف تبليغ
    public Boolean deleteAnnouncements(Long announcementsId){
        if (!announcementsRepo.findById(announcementsId).equals(Optional.empty())){
            announcementsRepo.deleteById(announcementsId);
            return true;
        }
        return false;
    }
}
