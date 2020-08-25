package net.huansi.equipment.equipmentapp.sqlite_db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by shanz on 2017/7/5.
 */
@Entity
public class RepairBaseDataInSQLite {
    @Id(autoincrement = true)
    private Long id;
    private int type;//0维护项目 1维修结果
    private String name;
    private String code;
    @Generated(hash = 1585836537)
    public RepairBaseDataInSQLite(Long id, int type, String name, String code) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.code = code;
    }
    @Generated(hash = 914633897)
    public RepairBaseDataInSQLite() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }


}
