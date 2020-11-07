package com.vtest.it.testerdatalogdealplatform.dao.mes;

import com.vtest.it.testerdatalogdealplatform.pojo.mes.CustomerCodeAndDeviceBean;
import com.vtest.it.testerdatalogdealplatform.pojo.mes.MesConfigBean;
import com.vtest.it.testerdatalogdealplatform.pojo.mes.SlotAndSequenceConfigBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author shawn.sun
 * @date 2020-11-7 20:03:09
 */
@Mapper
@Repository
public interface MesDao {
    public String getWaferIdBySlot(@Param("lot") String lot, @Param("slot") String slot);

    public SlotAndSequenceConfigBean getLotSlotConfig(@Param("lot") String lot);

    public MesConfigBean getWaferConfigFromMes(@Param("waferId") String waferId, @Param("cpProcess") String cpProcess);

    public CustomerCodeAndDeviceBean getCustomerAndDeviceByLot(@Param("lot") String lot);

    public CustomerCodeAndDeviceBean getCustomerAndDeviceByWaferAndCpStep(@Param("waferId") String waferId, @Param("cpStep") String cpStep);
}
