package com.bgw.testing.dao.bgw_automation.pojo;

public class TsMysqlInfoWithBLOBs extends TsMysqlInfo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_mysql_info.is_query
     *
     * @mbg.generated
     */
    private byte[] isQuery;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ts_mysql_info.is_single_result
     *
     * @mbg.generated
     */
    private byte[] isSingleResult;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_mysql_info.is_query
     *
     * @return the value of ts_mysql_info.is_query
     *
     * @mbg.generated
     */
    public byte[] getIsQuery() {
        return isQuery;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_mysql_info.is_query
     *
     * @param isQuery the value for ts_mysql_info.is_query
     *
     * @mbg.generated
     */
    public void setIsQuery(byte[] isQuery) {
        this.isQuery = isQuery;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ts_mysql_info.is_single_result
     *
     * @return the value of ts_mysql_info.is_single_result
     *
     * @mbg.generated
     */
    public byte[] getIsSingleResult() {
        return isSingleResult;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ts_mysql_info.is_single_result
     *
     * @param isSingleResult the value for ts_mysql_info.is_single_result
     *
     * @mbg.generated
     */
    public void setIsSingleResult(byte[] isSingleResult) {
        this.isSingleResult = isSingleResult;
    }
}