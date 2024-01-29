package gdsc.sc.bsafe.repository;

import gdsc.sc.bsafe.domain.Record;
import gdsc.sc.bsafe.domain.User;
import gdsc.sc.bsafe.domain.enums.RepairStatus;
import gdsc.sc.bsafe.domain.mapping.Repair;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {

    @Override
    Optional<Repair> findById(Long repairId);

    Optional<Repair> findByRecord(Record record);

    @Override
    void delete(Repair repair);

    Slice<Repair> findAllByRecord_UserOrderByCreatedAtDesc(User user);

    Slice<Repair> getByVolunteerAndStatus(User user, RepairStatus status);

    Slice<Repair> getByStatusAndOrderByLegalDistrict(RepairStatus status);

}
