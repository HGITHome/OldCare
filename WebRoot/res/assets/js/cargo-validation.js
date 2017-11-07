var FormValidation = function () {

    
    // basic validation
    var handleValidation1 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form1 = $('#form_sample_1');
            var error1 = $('.alert-danger', form1);
            var success1 = $('.alert-success', form1);

            form1.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                messages: {
                	cargoName:{
                		remote:"货柜号不能重复"
                    }
                },
                rules: {
                	cargoName: {
                        required: true,
                        maxlength:100,
                        remote:{
                            type:"POST",
                            url:"v_check_cargoName.do",             
                            data:{
                              veryCode:function(){return $("#cargoName").val();}
                            } 
                           } 
                    },
                    warningTime: {
                    	 required: true,
                    	digits:true
                    },
                    expectedBeginDate: {
                        required: true,
                        date:true
                    }, 
                    expectedEndDate: {
                        required: true,
                        date:true
                    }
                },

                invalidHandler: function (event, validator) { //display error alert on form submit              
                    success1.hide();
                    error1.show();
                    App.scrollTo(error1, -200);
                },

                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },

                success: function (label) {
                    label
                        .closest('.form-group').removeClass('has-error'); // set success class to the control group
                },

                submitHandler: function (form) {
                    success1.show();
                    error1.hide();
                    if(!confirm("您确定生成货柜?\n货柜号:【"+$("#cargoName").val()+"】?\n预计开始装载时间:"+$("#expectedBeginDate").val()+"\n预计结束装载时间:"+$("#expectedEndDate").val())) {
                		return;
                	}
                    form[0].submit(); // submit the form
                }
            });


    }

    
    // basic validation
    var handleValidation2 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form1 = $('#form_sample_2');
            var error1 = $('.alert-danger', form1);
            var success1 = $('.alert-success', form1);

            form1.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                messages: {
                },
                rules: {
                    warningTime: {
                    	 required: true,
                    	digits:true
                    },
                    expectedBeginDate: {
                        required: true,
                        date:true
                    }, 
                    expectedEndDate: {
                        required: true,
                        date:true
                    }
                },

                invalidHandler: function (event, validator) { //display error alert on form submit              
                    success1.hide();
                    error1.show();
                    App.scrollTo(error1, -200);
                },

                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },

                success: function (label) {
                    label
                        .closest('.form-group').removeClass('has-error'); // set success class to the control group
                },

                submitHandler: function (form) {
                    success1.show();
                    error1.hide();
                    if(!confirm("您确定修改货柜?\n货柜号:【"+$("#cargoName").val()+"】?\n预计开始装载时间:"+$("#expectedBeginDate").val()+"\n预计结束装载时间:"+$("#expectedEndDate").val())) {
                		return;
                	}
                    form[0].submit(); // submit the form
                }
            });


    }
    
    // basic validation
    var handleValidation3 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form1 = $('#form_uploadXLS');
            var error1 = $('.alert-danger', form1);
            var success1 = $('.alert-success', form1);

            form1.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                messages: {
                	file:{
                		  required: "上传文件不允许为空",  
                		  extension: "文件类型错误，请上传excel文件" 
                    }
                },
                rules: {
                	file: {  
                         required: true,  
                         extension: "xlsx|xls"  
                     }  
                },

                invalidHandler: function (event, validator) { //display error alert on form submit              
                    success1.hide();
                    error1.show();
                    App.scrollTo(error1, -200);
                },

                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },

                success: function (label) {
                    label
                        .closest('.form-group').removeClass('has-error'); // set success class to the control group
                },

                submitHandler: function (form) {
                    success1.show();
                    error1.hide();
                    form[0].submit(); // submit the form
                }
            });


    }
  


    return {
        //main function to initiate the module
        init: function () {
            handleValidation1();
            handleValidation2();
            handleValidation3();
        }

    };

}();

jQuery.validator.methods.compareDate = function(value, element, param) {
    //var startDate = jQuery(param).val() + ":00";补全yyyy-MM-dd HH:mm:ss格式
    //value = value + ":00";
    
    var startDate = jQuery(param).val();
    
    var date1 = new Date(Date.parse(startDate.replace("-", "/")));
    var date2 = new Date(Date.parse(value.replace("-", "/")));
    return date1 < date2;
};

jQuery.validator.methods.date = function (value, element) {
    var matches = /(\d{4})[-\/](\d{2})[-\/](\d{2})/.exec(value);
    if (matches == null) return this.optional(element)|| false;
    return this.optional(element) || true;
};

jQuery(document).ready(function() {
    FormValidation.init();
});