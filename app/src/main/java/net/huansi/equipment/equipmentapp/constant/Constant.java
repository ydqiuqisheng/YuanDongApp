package net.huansi.equipment.equipmentapp.constant;

import android.Manifest;

import java.util.UUID;

/**
 * Created by 单中年 on 2017/2/22.
 */

public class Constant {
    public static final String STATUS_OK = "OK";
    public static final int BEEPER_ATTR_NUM = 140;
    public static final String DATA_BLUETOOTH_DEVICE = "com.rfidreader.data.bluetooth.device";
//    public final static String IP="10.17.98.230";
//    public final static String IP="192.168.2.166";
//    public final static String IP="192.168.1.102" ;
    public final static String CHECK_CODE="4A8D54D1-C7A1-48DC-AD7E-8CA1686FBF85";
    public final static String DB_NAME="HSEquipment";//SQLite的数据库名称

    public static final String RFD8500 = "RFD8500";

    // 所需的全部权限
    public static final String[] PERMISSIONS = new String[]{
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE
//            Manifest.permission.READ_CONTACTS
    };

//    public static final String NAME = "RFIDBluetooth";
    // Unique UUID for Reader BT SPP
//    public static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    public static final UUID CUSTOM_UUID = UUID.fromString("2ad8a392-0e49-e52c-a6d2-60834c012263");

//    public static final String DEVICE_MODEL = "MODEL_NAME";
//    public static final String DEVICE_SERIAL = "SERIAL_NUMBER";

    public static final String PICTURE_ONLINE_HEAD="";//服务器图片的头部

    public static final int PICTURE_ALL_NUMBER_PER=2;//每一单维修只能上传两张图片
    public static final int PICTURE_UP_NUMBER_PER=4;//每一流程只能上传两张图片

//    /**
//     * 角色常量
//     */
//    public final static class RoleConstants{
//        public static final String ADMIN="admin";//管理员
//        public static final String NORMAL="normal";//普通人员
//    }

//    /**
//     * 登陆界面的常量
//     */
//    public static final class LoginActivityConstants{
////        public final static String USER_NO_SP_KEY="user_no_sp";//用户名的SharedPreferences的key
//    }
//
//    /**
//     * 用户管理的常量
//     */
//    public static final class UserManageActivityConstants{
//        public static final String LIST_PARAM="list";//向下传的数据
//        public static final String IS_USER_PARAM="isUser";//是否是用户管理操作
//        public static final int MANAGE_USER_ROLE_QUEST_CODE=101;//管理用户或者角色的请求码
//    }
//
//    /**
//     * UserOrRoleManageActivity的常量
//     */
//    public static final class UserOrRoleManageActivityConstants{
//        public static final String UPDATE_USER_PARAM="update_user";//更新用户
//    }
//
    /**
     * UserRoleActivity的常量
     */
    public static final class UserRoleActivityConstants{
        public static final String FRAGMENT_USER_LIST_PARAM="fragment_user_list";//用户的数据
        public static final String FRAGMENT_ROLE_LIST_PARAM="fragment_role_list";//用户的数据

        public static final int ADD_USER_QUEST_CODE=201;//添加用户的请求码
        public static final int ADD_ROLE_QUEST_CODE=202;//添加角色的请求码
    }

    /**
     * InventoryMainActivity的常量
     */
    public static final class InventoryMainActivityConstants{
        public static final String INVENTORY_ID_IN_SQLITE_PARAM="inventory_id_in_sqlite";//Sqlte的盘点主表的id
    }

    public static final class InventoryDetailActivityConstants{
        public static final int TRIGGER_OPERATION_INDEX=301;//操作trigger
        public static final int RECEIVE_DATA_INDEX=302;//接收到数据
        public static final int AREA_REQUEST_CODE=303;//进入区域列表
    }

    /**
     * RepairDetailActivity 的常量
     */
    public final static class RepairDetailActivityConstants{
        public static final int ADD_DETAIL_QUEST_CODE=401;//添加明细的请求码
        public static final int UPDATE_DETAIL_QUEST_CODE=402;//修改明细的请求码
        public static final String PLAN_PARAM="plan";//计划的参数
        public static final String SQLITE_DATA_PARAM="SQLite_data";//本地保存的数据
    }

    /**
     * RepairDetailAddActivity的常量
     */
    public final static class RepairDetailAddActivityConstants{
        public static final String RETURN_DATA_KEY="repair_detail_data";//返回的数据
        public static final String DETAIL_DATA_PARAM="detail_data";//传过来的明细数据
        public static final String DETAIL_DATA_POSITION_PARAM="detail_data_position";//传过来的明细数据的位置
//        public static final String PICTURE_QTY_PARAM="picture_qty";//图片的数量

    }

    /**
     * 图片放大的常量
     */
    public final static class LargerImageSHowActivityConstants{
        public static final String URL_PATH_PARAM="pathOrUrl";//图片的地址或者网址
    }
    /**
     * 文本放大的常量
     */
    public final static class LargerTextSHowActivityConstants{
        public static final String TEXT_CONTENT_PARAM="contentOrDetail";//文本的内容
    }
//    /**
//     * RepairAddPlanActivity的常量
//     */
//    public final static class RepairAddPlanActivityConstants{
//        public static final String PLAN_EP_INFO_PARAM="plan_ep_info";//计划的信息
//    }

    /**
     * BindCardDetailActivity的常量
     */
    public final static class BindCardDetailActivityConstants{
        public static final String EQUIPMENT_INFO_PARAM="equipment_info";//传过来的设备
        public static final String RFID_HAS_BIND="RFID_has_bind";//已经绑定过的RFID
        public static final String POSITION_IN_ALL_PARAM="position_in_all";//在全部数据的位置
        public static final String POSITION_IN_SHOW_PARAM="position_in_show";//在显示数据的位置
        public static final String RETURN_DATA_KEY="return_data";//返回的数据
    }
    /**
     * BindCardDetailActivity的常量
     */
    public final static class CheckMainActivityConstants{
        public static final String EQUIPMENT_INFO_PARAM="equipment_info";//传过来的设备
        public static final String RFID_HAS_BIND="RFID_has_bind";//已经绑定过的RFID
        public static final String POSITION_IN_ALL_PARAM="position_in_all";//在全部数据的位置
        public static final String POSITION_IN_SHOW_PARAM="position_in_show";//在显示数据的位置
        public static final String RETURN_DATA_KEY="return_data";//返回的数据
    }


    public final static class BindCardMainActivityConstants{
        public static final int BIND_CARD_QUEST_CODE=501;//请求码
    }

    public final static class MailActivityConstants{
        public static final String MAIL_DATA_PARAM="mail_data";//邮件的内容
    }

    public final static class UserActivityConstants{
        public static final int ADD_USER_REQUEST_CODE=601;//添加用户
        public static final int UPDATE_USER_REQUEST_CODE=602;//修改用户
        public static final String USER_DATA_PARAM="user_data";//用户的信息
    }


    public final static class RoleActivityConstants{
        public static final int ADD_ROLE_REQUEST_CODE=701;//添加组别
        public static final int UPDATE_ROLE_REQUEST_CODE=702;//更新组别
        public static final String GROUP_DATA_PARAM="group_data";//组别的信息
        public static final String FACTORY_LIST_PARAM="factory_list";//工厂的列表
    }

    public final static class InventoryAreaListActivityConstants{
        public static final String RETURN_AREA_DATA="return_area_data";//返回的数据
    }




}
