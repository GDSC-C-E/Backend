package gdsc.sc.bsafe.service;

import gdsc.sc.bsafe.domain.Organization;
import gdsc.sc.bsafe.domain.User;
import gdsc.sc.bsafe.domain.enums.Authority;
import gdsc.sc.bsafe.domain.enums.RepairStatus;
import gdsc.sc.bsafe.domain.enums.VolunteerType;
import gdsc.sc.bsafe.domain.mapping.Repair;
import gdsc.sc.bsafe.domain.mapping.Volunteer;
import gdsc.sc.bsafe.global.exception.CustomException;
import gdsc.sc.bsafe.global.exception.enums.ErrorCode;
import gdsc.sc.bsafe.repository.RepairRepository;
import gdsc.sc.bsafe.repository.VolunteerRepository;
import gdsc.sc.bsafe.web.dto.common.SliceResponse;
import gdsc.sc.bsafe.web.dto.request.VolunteerUserRequest;
import gdsc.sc.bsafe.web.dto.response.RepairItemResponse;
import gdsc.sc.bsafe.web.dto.response.VolunteerRepairListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final RepairRepository repairRepository;
    private final OrganizationService organizationService;
    private final RepairService repairService;

    @Transactional
    public Long saveVolunteer(User user, VolunteerUserRequest request) {
        Volunteer volunteer = createVolunteer(user, VolunteerType.valueOf(request.getType()));

        String organizationName = request.getOrganization_name();
        if (organizationName != null && !organizationName.isEmpty()) {
            Organization organization = organizationService.findOrCreateOrganization(organizationName);
            volunteer.updateOrganization(organization);
        }

        Volunteer savedVolunteer = volunteerRepository.save(volunteer);

        return savedVolunteer.getUser().getUserId();
    }

    @Transactional
    public Long volunteerRepair(User user, Long repairId) {
        Repair repair = repairService.findByRepairId(repairId);

        updateVolunteer(user, repair);
        repairService.updateRepairStatus(repairId, RepairStatus.PROCEEDING);

        return repair.getRepairId();
    }

    public VolunteerRepairListResponse getVolunteerRepairList(User user) {
        Slice<Repair> proceedingRepairs = repairRepository.getByVolunteerAndStatus(user, RepairStatus.PROCEEDING);
        SliceResponse<RepairItemResponse> proceedingRepairsList = new SliceResponse<>(proceedingRepairs.map(RepairItemResponse::new));

        Slice<Repair> completeRepairs = repairRepository.getByVolunteerAndStatus(user, RepairStatus.COMPLETE);
        SliceResponse<RepairItemResponse> completeRepairsList = new SliceResponse<>(completeRepairs.map(RepairItemResponse::new));

        VolunteerRepairListResponse response= new VolunteerRepairListResponse();
        response.setProceeding_repair(proceedingRepairsList);
        response.setComplete_repair(completeRepairsList);

        return response;
    }

    private Volunteer createVolunteer(User user, VolunteerType type) {
        user.updateUserAuthority(Authority.VOLUNTEER);

        return Volunteer.builder()
                .user(user)
                .type(type)
                .build();
    }

    private void updateVolunteer(User user, Repair repair) {
        validateVolunteerUser(user);
        repair.updateVolunteer(user);
    }

    private void validateVolunteerUser(User user) {
        if (!user.getAuthority().equals(Authority.VOLUNTEER)) {
            throw new CustomException(ErrorCode.NOT_VOLUNTEER_USER);
        }
    }
}