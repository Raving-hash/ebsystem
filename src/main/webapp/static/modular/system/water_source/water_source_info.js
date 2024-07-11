/**
 * 角色详情对话框（可用于添加和修改对话框）
 */
var WaterSourceInfoDlg = {
    waterSourceInfoData: {},
    validateFields: {
        name: {
            validators: {
                notEmpty: {
                    message: '用户名不能为空'
                }
            }
        },
        phone: {
            validators: {
                notEmpty: {
                    message: '联系方式不能为空'
                }
            }
        },
    }
};

/**
 * 清除数据
 */
WaterSourceInfoDlg.clearData = function () {
    this.waterSourceInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WaterSourceInfoDlg.set = function (key, val) {
    this.waterSourceInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WaterSourceInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
WaterSourceInfoDlg.close = function () {
    parent.layer.close(window.parent.WaterSource.layerIndex);
};



/**
 * 收集数据
 */
WaterSourceInfoDlg.collectData = function () {
    this.set('id').set('name').set('phone');
};

/**
 * 验证数据是否为空
 */
WaterSourceInfoDlg.validate = function () {
    $('#waterSourceInfoForm').data("bootstrapValidator").resetForm();
    $('#waterSourceInfoForm').bootstrapValidator('validate');
    return $("#waterSourceInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加用户
 */
WaterSourceInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/waterSource/add", function (data) {
        Feng.success("添加成功!");
        window.parent.WaterSource.table.refresh();
        WaterSourceInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    // 获取WaterSourceInfoData的name属性的值
    ajax.set(this.waterSourceInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
WaterSourceInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/waterSource/edit", function (data) {
        Feng.success("修改成功!");
        window.parent.WaterSource.table.refresh();
        WaterSourceInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.waterSourceInfoData);
    ajax.start();
};

$(function () {
    Feng.initValidator("waterSourceInfoForm", WaterSourceInfoDlg.validateFields);
    var defaultColunms = WaterSource.initColumn();
    var table = new BSTable(WaterSource.id, "/waterSource/list", defaultColunms);
    table.setPaginationType("client");
    table.init();
    WaterSource.table = table;
});

