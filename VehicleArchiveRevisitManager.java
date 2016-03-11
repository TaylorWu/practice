package com.zf.qqcy.dataService.vehicle.ms.serverCar.service;

import com.zf.qqcy.dataService.vehicle.ms.serverCar.dao.AccountRevisitRemindViewDao;
import com.zf.qqcy.dataService.vehicle.ms.serverCar.dao.RevisitRemindViewDao;
import com.zf.qqcy.dataService.vehicle.ms.serverCar.dao.VehicleArchiveRevisitDao;
import com.zf.qqcy.dataService.vehicle.ms.serverCar.entity.AccountRevisitRemindView;
import com.zf.qqcy.dataService.vehicle.ms.serverCar.entity.RevisitRemindView;
import com.zf.qqcy.dataService.vehicle.ms.serverCar.entity.VehicleArchiveRevisit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
@Transactional(readOnly = true)
public class VehicleArchiveRevisitManager {

	@Autowired
	private VehicleArchiveRevisitDao vehicleArchiveRevisitDao;
    @Autowired
    private RevisitRemindViewDao revisitRemindViewDao;
    @Autowired
    private AccountRevisitRemindViewDao accountRevisitRemindViewDao;

    @PersistenceContext()
    private EntityManager em;
	
	public Page<VehicleArchiveRevisit> findByFilter(Specification<VehicleArchiveRevisit> spec, PageRequest pageRequest) {
        return vehicleArchiveRevisitDao.findAll(spec, pageRequest);
    }
	
	@Transactional
    public void save(VehicleArchiveRevisit vehicleArchiveRevisit) {
		vehicleArchiveRevisitDao.save(vehicleArchiveRevisit);
    }

    public Page<RevisitRemindView> findRemindByFilter(Specification<RevisitRemindView> spec, PageRequest pageRequest){
        return revisitRemindViewDao.findAll(spec, pageRequest);
    }

    public Page<AccountRevisitRemindView> findAccountRemindByFilter(Specification<AccountRevisitRemindView> spec, PageRequest pageRequest){
        return accountRevisitRemindViewDao.findAll(spec, pageRequest);
    }

    public List statisticsRevisit(Specification<VehicleArchiveRevisit> spec) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object> query = builder.createQuery();
        Root<VehicleArchiveRevisit> root = query.from(VehicleArchiveRevisit.class);
        query.multiselect(root.get("revisitPerson"), builder.count(root.get("vehicleArchive")));
        query.groupBy(root.get("revisitPerson"));
        query.where(spec.toPredicate(root, query, builder));
        return em.createQuery(query).getResultList();
    }
}
