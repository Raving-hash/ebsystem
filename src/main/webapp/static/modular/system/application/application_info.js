/**
 * 角色详情对话框（可用于添加和修改对话框）
 */
var ApplicationInfoDlg = {
    applicationInfoData: {},
    validateFields: {
        deviceId: {
            validators: {
                notEmpty: {
                    message: '用户名不能为空'
                }
            }
        },
        type: {
            validators: {
                notEmpty: {
                    message: '申请类型不能为空'
                }
            },
        }
    }
};

/**
 * 清除数据
 */
ApplicationInfoDlg.clearData = function () {
    this.applicationInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ApplicationInfoDlg.set = function (key, val) {
    this.applicationInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ApplicationInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
ApplicationInfoDlg.close = function () {
    parent.layer.close(window.parent.Application.layerIndex);
};



/**
 * 收集数据
 */
ApplicationInfoDlg.collectData = function () {
    this.set('id').set('deviceId').set('type');
};

/**
 * 验证数据是否为空
 */
ApplicationInfoDlg.validate = function () {
    $('#applicationInfoForm').data("bootstrapValidator").resetForm();
    $('#applicationInfoForm').bootstrapValidator('validate');
    return $("#applicationInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加用户
 */
ApplicationInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/application/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Application.table.refresh();
        ApplicationInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    // 获取ApplicationInfoData的name属性的值
    ajax.set(this.applicationInfoData);
    ajax.start();
};

/**
 * 提交修改
 */
ApplicationInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/application/edit", function (data) {
        Feng.success("修改成功!");
        window.parent.Application.table.refresh();
        ApplicationInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.applicationInfoData);
    ajax.start();
};


$(function () {
    Feng.initValidator("applicationInfoForm", ApplicationInfoDlg.validateFields);
    var defaultColunms = Application.initColumn();
    var table = new BSTable(Application.id, "/application/getApplicationsByUser", defaultColunms);
    table.setPaginationType("client");
    table.init();
    Application.table = table;
});

