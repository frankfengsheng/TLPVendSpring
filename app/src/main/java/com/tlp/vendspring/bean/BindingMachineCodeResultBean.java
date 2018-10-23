package com.tlp.vendspring.bean;

public class BindingMachineCodeResultBean {


    /**
     * data : {"machine_code":"10020030014"}
     * status : 200
     * msg : 成功
     */

    private DataBean data;
    private int status;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * machine_code : 10020030014
         */

        private String machine_code;

        public String getMachine_code() {
            return machine_code;
        }

        public void setMachine_code(String machine_code) {
            this.machine_code = machine_code;
        }
    }
}
