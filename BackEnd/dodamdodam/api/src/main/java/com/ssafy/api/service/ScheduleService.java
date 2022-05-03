package com.ssafy.api.service;

import com.ssafy.core.dto.req.NewScheduleReqDto;
import com.ssafy.core.dto.res.ScheduleDetailResDto;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.Schedule;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.CustomErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import com.ssafy.core.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import static com.ssafy.core.exception.CustomErrorCode.INVALID_REQUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ProfileRepository profileRepository;
    private final FamilyRepository familyRepository;

    public void createSchedule(NewScheduleReqDto scheduleReq, Family family, User user) {
        Profile profile = profileRepository.findProfileByUserPk(user.getUserPk());
        LocalDate startDate = stringToLocalDate(scheduleReq.getStartDate());
        LocalDate endDate;
        if (scheduleReq.getEndDate().length() > 0) {
            endDate = stringToLocalDate(scheduleReq.getEndDate());
        } else {
            endDate = startDate;
        }
        scheduleRepository.save(Schedule.builder()
                .title(scheduleReq.getTitle())
                .content(scheduleReq.getContent())
                .startDate(startDate)
                .endDate(endDate)
                .family(family)
                .role(profile.getRole())
                .build());
    }

    public LocalDate stringToLocalDate(String localDate) {
        String[] localDateList = localDate.split("-");
        LocalDate result;
        try {
            if (localDateList[0].length() != 4 || localDateList[1].length() != 2 || localDateList[2].length() != 2) {
                throw new CustomException(INVALID_REQUEST, "잘못된 날짜 입력 방식입니다.");
            }
            result = LocalDate.of(
                    Integer.parseInt(localDateList[0]),
                    Integer.parseInt(localDateList[1]),
                    Integer.parseInt(localDateList[2]));
        } catch (NumberFormatException | DateTimeException e) {
            throw new CustomException(INVALID_REQUEST);
        }
        return result;
    }

    public Schedule getSchedule (long scheduleId, Family family) {
        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);
        if (schedule == null) {
            throw new CustomException(INVALID_REQUEST, "해당 일정이 없습니다.");
        } else if (schedule.getFamily().getId() != family.getId()) {
            throw new CustomException(CustomErrorCode.NOT_BELONG_FAMILY);
        }
        return scheduleRepository.findScheduleById(scheduleId);
    }


    public List<ScheduleDetailResDto> getScheduleListByUserPkAndDay (Long userPk, String day) {
        Family family = familyRepository.findFamilyByUserPk(userPk);

        String[] dayList = day.split("-");
        LocalDate result;
        try {
            if (dayList[0].length() != 4 || dayList[1].length() != 2 || dayList[2].length() != 2) {
                throw new CustomException(INVALID_REQUEST, "잘못된 날짜 입력 방식입니다.");
            }
            result = LocalDate.of(
                    Integer.parseInt(dayList[0]),
                    Integer.parseInt(dayList[1]),
                    Integer.parseInt(dayList[2]));
        } catch (NumberFormatException | DateTimeException e) {
            throw new CustomException(INVALID_REQUEST);
        }
        List<ScheduleDetailResDto> scheduleRes = scheduleRepository.findScheduleByDay(result, family);
        if (scheduleRes.isEmpty()) {
            throw new CustomException(INVALID_REQUEST, "해당하는 스케줄이 없습니다.");
        }
        return scheduleRes;
    }

    public List<ScheduleDetailResDto> getScheduleListByMonth (Family family, String month) {
        String[] dayList = month.split("-");
        try {
            if (dayList[0].length() != 4 || dayList[1].length() != 2) {
                throw new CustomException(INVALID_REQUEST, "잘못된 날짜 입력 방식입니다.");
            }
        } catch (NumberFormatException | DateTimeException e) {
            throw new CustomException(INVALID_REQUEST);
        }
        Integer result = Integer.parseInt(dayList[1]);
        List<ScheduleDetailResDto> scheduleRes = scheduleRepository.findScheduleByMonth(result, family);
        if (scheduleRes.isEmpty()) {
            throw new CustomException(INVALID_REQUEST, "해당하는 스케줄이 없습니다.");
        }
        return scheduleRes;
    }

    public void updateSchedule(Schedule schedule, NewScheduleReqDto scheduleReq) {
        LocalDate startDate = stringToLocalDate(scheduleReq.getStartDate());
        LocalDate endDate;
        if (scheduleReq.getEndDate().length() > 0) {
            endDate = stringToLocalDate(scheduleReq.getEndDate());
        } else {
            endDate = startDate;
        }
        schedule.setTitle(scheduleReq.getTitle());
        schedule.setContent(scheduleReq.getContent());
        schedule.setStartDate(startDate);
        schedule.setEndDate(endDate);
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }
}
