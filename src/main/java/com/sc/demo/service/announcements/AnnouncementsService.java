package com.sc.demo.service.announcements;

import com.sc.demo.model.Tokens.AppToken;
import com.sc.demo.model.announcements.Announcements;
import com.sc.demo.model.announcements.AnnouncementsAttachment;
import com.sc.demo.model.announcements.AnnouncementsDetails;
import com.sc.demo.model.announcements.Pin;
import com.sc.demo.model.dto.announcements.AllAnnouncementsFamilyRequest;
import com.sc.demo.model.dto.announcements.AnnouncementsRequest;
import com.sc.demo.model.dto.announcements.PhoneAnnouncementsRequest;
import com.sc.demo.model.dto.announcements.AnnouncementsTokenRequest;
import com.sc.demo.model.notification.SendingType;
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
        var employeesId = tokenService.decodeToken(token.substring(7)).getSubject();

        Announcements announcements = new Announcements(announcementsRequest.title(),
                announcementsRequest.description(), announcementsRequest.sendingType() == SendingType.BRANCH ? announcementsRequest.branches() : null,
                announcementsRequest.sendingType(), Long.parseLong(employeesId));

        System.out.println(announcements);
        announcements = announcementsRepo.save(announcements);


        if (announcementsRequest.sendingType() == SendingType.PRIVATE) {
            for (Long a : userId) {
                announcementsDetailsRepo.save(new AnnouncementsDetails(a, Long.parseLong(employeesId), announcements));
            }
        }else if (announcementsRequest.sendingType() == SendingType.BRANCH) {
            String getUsersInBranch = announcementsRequest.branches();
            List<AnnouncementsTokenRequest> getToken = jdbcClient.sql("""
                    SELECT T.TOKEN AS token
                          ,U.USER_ID AS userId
                    FROM MOBAPP.SC_APP_USER U
                    LEFT JOIN MOBAPP.SC_APP_TOKEN T on (U.USER_ID = T.USER_ID)
                    WHERE U.BRANCHES = :branch
                    """)
                    .param("branch", getUsersInBranch)
                    .query(AnnouncementsTokenRequest.class)
                    .list();
            for (AnnouncementsTokenRequest b : getToken ) {
                announcementsDetailsRepo.save(new AnnouncementsDetails(b.userId(), Long.parseLong(employeesId), announcements));
            }
        }

        if (file != null) try {
            String originalFilename = file.getOriginalFilename();
            String newFilename = System.nanoTime() + originalFilename.substring(originalFilename.lastIndexOf("."));
            String filePath = environment.getProperty("ATTACHMENT_PATH_ANNOUNCEMENTS") + newFilename;
            announcementsAttachmentRepo.save(new AnnouncementsAttachment(newFilename, Long.parseLong(employeesId), announcements));
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return announcements;
    }

    // حفظ Token القادم من firebase في قاعدة البيانات
    public long saveToken(AnnouncementsTokenRequest announcementsTokenRequest) {
        Optional<AppToken> byToken = announcementsTokenRepo.findById(announcementsTokenRequest.userId());

        if (byToken.isPresent()) {
            AppToken appToken = byToken.get();
            appToken.setLastUpdate(LocalDateTime.now());
            appToken.setToken(announcementsTokenRequest.token());
            return announcementsTokenRepo.save(appToken).getUserId();
        } else {
            AppToken appToken = new AppToken();
            appToken.setToken(announcementsTokenRequest.token());
            appToken.setUserId(announcementsTokenRequest.userId());
            return announcementsTokenRepo.save(appToken).getUserId();
        }
    }

    // تبليغات التطبيق لكل يوزر
    public List<PhoneAnnouncementsRequest> PhoneAnnouncements(String token) {
        var userTokenId = tokenService.decodeToken(token.substring(7)).getSubject();

        return jdbcClient.sql("""
                   SELECT A.CREATE_DATE AS createDate
                          ,A.TITLE
                          ,A.DESCRIPTION
                          ,TO_CHAR(:path) || AA.FILE_NAME AS fileName
                          ,A.PIN AS pin
                   FROM SC_ANNOUNCEMENTS A
                   LEFT JOIN SC_ANNOUNCEMENTS_DETAILS AD ON A.ANNOUNCEMENTS_ID = AD.ANNOUNCEMENTS_ID
                   LEFT JOIN SC_ANNOUNCEMENTS_ATTACHMENT AA ON A.ANNOUNCEMENTS_ID = AA.ANNOUNCEMENTS_ID
                   WHERE AD.USER_ID = :userId OR A.SENDING_TYPE = 0
                   ORDER BY A.PIN DESC, A.CREATE_DATE DESC
                """)
                .param("userId", userTokenId)
                .param("path", "http://37.239.42.53:1801/socialCare/V1/api/sc/photoAnnouncements/")
                .query(PhoneAnnouncementsRequest.class)
                .list();
    }

    // الداش بورد جميع التبليغات التي تصل للعائلة اذا كانت خاصة او عامة
    public List<AllAnnouncementsFamilyRequest> AllAnnouncementsFamily() {
        return jdbcClient.sql("""
                   SELECT A.ANNOUNCEMENTS_ID AS announcementsId
                         ,A.CREATE_DATE AS createDate
                         ,A.TITLE
                         ,TO_CHAR(:path) || AA.FILE_NAME AS fileName
                         ,A.DESCRIPTION
                         ,A.SENDING_TYPE AS sendingType
                         ,A.PIN AS pin
                   FROM SC_ANNOUNCEMENTS A
                   LEFT JOIN SC_ANNOUNCEMENTS_DETAILS AD ON A.ANNOUNCEMENTS_ID = AD.ANNOUNCEMENTS_ID
                   LEFT JOIN SC_ANNOUNCEMENTS_ATTACHMENT AA ON A.ANNOUNCEMENTS_ID = AA.ANNOUNCEMENTS_ID
                   ORDER BY A.PIN DESC, A.CREATE_DATE DESC
                """)
                .param("path", "http://37.239.42.53:1801/socialCare/V1/api/sc/allAnnouncementsPhotos/")
                .query(AllAnnouncementsFamilyRequest.class)
                .list();

    }

    // حذف تبليغ
    public Boolean deleteAnnouncement(Long announcementsId){
        if (!announcementsRepo.findById(announcementsId).equals(Optional.empty())){
            announcementsRepo.deleteById(announcementsId);
            return true;
        }
        return false;
    }

    // تعديل تبليغ
    public Boolean editAnnouncement(Long announcementId, String title, String description,
                                          MultipartFile file, String token){

        var employeesId = tokenService.decodeToken(token.substring(7)).getSubject();

        Announcements updateAnnouncement = announcementsRepo.findById(announcementId).get();
        if (title != null) {
            updateAnnouncement.setTitle(title);
        }
        if (description != null) {
            updateAnnouncement.setDescription(description);
        }
        updateAnnouncement.setLastUpdateBy(Long.parseLong(employeesId));
        updateAnnouncement.setLastUpdate(LocalDateTime.now());

        if (file != null) try {
        AnnouncementsAttachment updateAnnouncementAttachment = announcementsAttachmentRepo.findByAnnouncements(updateAnnouncement);
        String originalFilename = file.getOriginalFilename();
        String newFilename = System.nanoTime() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String filePath = environment.getProperty("ATTACHMENT_PATH_ANNOUNCEMENTS") + newFilename;

        updateAnnouncementAttachment.setFileName(newFilename);
        updateAnnouncementAttachment.setLastUpdate(LocalDateTime.now());
        updateAnnouncementAttachment.setLastUpdateBy(Long.parseLong(employeesId));
        updateAnnouncementAttachment.setAnnouncements(updateAnnouncement);
        announcementsAttachmentRepo.save(updateAnnouncementAttachment);

        file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        announcementsRepo.save(updateAnnouncement);
        return true;
    }

    // تثبيت او الغاء تثبيت التبليغ
    public Boolean editAnnouncementPin(Long announcementId, String token){
        var employeesId = tokenService.decodeToken(token.substring(7)).getSubject();

        Announcements pinAnnouncement = announcementsRepo.findById(announcementId).get();
        pinAnnouncement.setPin(pinAnnouncement.getPin() != Pin.PIN ? Pin.PIN : Pin.NOTPIN);
        pinAnnouncement.setLastUpdateBy(Long.parseLong(employeesId));
        pinAnnouncement.setLastUpdate(LocalDateTime.now());

        announcementsRepo.save(pinAnnouncement);
        return true;
    }
}
