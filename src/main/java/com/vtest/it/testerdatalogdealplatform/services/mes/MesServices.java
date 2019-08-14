package com.vtest.it.testerdatalogdealplatform.services.mes;



import com.vtest.it.testerdatalogdealplatform.pojo.mes.CustomerCodeAndDeviceBean;
import com.vtest.it.testerdatalogdealplatform.pojo.mes.MesConfigBean;
import com.vtest.it.testerdatalogdealplatform.pojo.mes.SlotAndSequenceConfigBean;

public interface MesServices {
    public String getWaferIdBySlot(String lot, String slot);
    public SlotAndSequenceConfigBean getLotSlotConfig(String lot);
    public MesConfigBean getWaferConfigFromMes(String waferId, String cpProcess);
    public CustomerCodeAndDeviceBean getCustomerAndDeviceByLot(String lot);
    public CustomerCodeAndDeviceBean getCustomerAndDeviceByWaferAndCpStep(String waferId, String cpStep);
}
