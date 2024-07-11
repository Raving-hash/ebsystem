/**
 * 角色详情对话框（可用于添加和修改对话框）
 */
var PollutionInfoDlg = {
    pollutionInfoData: {},
};

/**
 * 清除数据
 */
PollutionInfoDlg.clearData = function () {
    this.pollutionInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PollutionInfoDlg.set = function (key, val) {
    this.pollutionInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PollutionInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
PollutionInfoDlg.close = function () {
    parent.layer.close(window.parent.Pollution.layerIndex);
};



/**
 * 收集数据
 */
PollutionInfoDlg.collectData = function () {

};

/**
 * 初始化表格的列
 */
PollutionInfoDlg.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '污染类型', field: 'pollTypeName', align: 'center', valign: 'middle', sortable: false},
        {title: '污染值', field: 'value', align: 'center', valign: 'middle', sortable: true},
        {title: '单位', field: 'pollTypeUnit', align: 'center', valign: 'middle', sortable: false},
        {title: '创建时间', field: 'updatetime', align: 'center', valign: 'middle', sortable: true},
        {title: '水源', field: 'waterSourceName', align: 'center', valign: 'middle', sortable: false},
        {title: '水源类型', field: 'waterTypeName', align: 'center', valign: 'middle', sortable: false},
    ]
    return columns;
};

$(function () {
    var defaultColunms = PollutionInfoDlg.initColumn();
    var url = "/pollution/selectPollutionsByPollutionTypeIdInDays?pollutionTypeId=2&days=7";
    var table = new BSTable(window.parent.Pollution.id, url, defaultColunms);
    table.setPaginationType("client");
    table.init();
    window.parent.Pollution.table = table;
});

