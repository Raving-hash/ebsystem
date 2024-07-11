/**
 * 角色详情对话框（可用于添加和修改对话框）
 */
var DeviceInfoDlg = {
    deviceInfoData: {},
    validateFields: {
        name: {
            validators: {
                notEmpty: {
                    message: '用户名不能为空'
                }
            }
        },
    }
};

/**
 * 清除数据
 */
DeviceInfoDlg.clearData = function () {
    this.deviceInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
DeviceInfoDlg.set = function (key, val) {
    this.deviceInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
DeviceInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
DeviceInfoDlg.close = function () {
    parent.layer.close(window.parent.Device.layerIndex);
};



/**
 * 收集数据
 */
DeviceInfoDlg.collectData = function () {
    this.set('id').set('name');
};

/**
 * 验证数据是否为空
 */
DeviceInfoDlg.validate = function () {
    $('#deviceInfoForm').data("bootstrapValidator").resetForm();
    $('#deviceInfoForm').bootstrapValidator('validate');
    return $("#deviceInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加用户
 */
DeviceInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/device/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Device.table.refresh();
        DeviceInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    // 获取DeviceInfoData的name属性的值
    ajax.set(this.deviceInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
DeviceInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/device/edit", function (data) {
        Feng.success("修改成功!");
        window.parent.Device.table.refresh();
        DeviceInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.deviceInfoData);
    ajax.start();
};

/**
 * 初始化表格的列
 */
DeviceInfoDlg.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '设备名称', field: 'name', align: 'center', valign: 'middle', sortable: false},
    ]
    return columns;
};

$(function () {
    Feng.initValidator("deviceInfoForm", DeviceInfoDlg.validateFields);
    var defaultColunms = DeviceInfoDlg.initColumn();
    var table = new BSTable(Device.id, "/device/list", defaultColunms);
    table.setPaginationType("client");
    table.init();
    Device.table = table;
});

