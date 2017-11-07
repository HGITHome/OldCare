var FormValidation = function () {

    // basic validation
	var handleValidation1 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form1 = $('#form_save');
            var error1 = $('.alert-danger', form1);
            var success1 = $('.alert-success', form1);

            form1.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                messages: {
                	photoFile:{
                		 /* required: "上传文件不允许为空", */ 
                		  extension: "文件类型错误，请上传gif,bmp,png,jpg,jpeg文件" 
                    },
            		videoFile:{
            			/*required:"上传文件不允许为空",*/
            			extension:"文件类型错误，请上传wmv，mp4，avi，rmvb文件"   			
            		},
            		record_time:{
            			required:"日期不能为空"
            		},
            		oldId:{
            			required:"请选择老人"
            		}
            		
                },
                rules: {
                	photoFile: {  
                       /*  required: true,  */
                         extension: "gif|png|jpg|jpeg|bmp"  
                     },
                     videoFile:{
                    	/* required:true,*/
                    	 extension:"rmvb|avi|wmv|mp4"
                     },
                     record_time:{
                    	 required:true
                     },
                     oldId:{
                    	 required:true
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


    };
    
    var handleValidation2 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form1 = $('#form_update');
            var error1 = $('.alert-danger', form1);
            var success1 = $('.alert-success', form1);

            form1.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                messages: {
                	photoFile:{
                		 /* required: "上传文件不允许为空",  */
                		  extension: "文件类型错误，请上传gif,bmp,png,jpg,jpeg文件" 
                    },
            		videoFile:{
            			/*required:"上传文件不允许为空",*/
            			extension:"文件类型错误，请上传wmv，mp4，avi，rmvb文件"   			
            		},
            		record_time:{
            			required:"日期不能为空"
            		},
            		oldId:{
            			required:"请选择来人"
            		}
            		
                },
                rules: {
                	photoFile: {  
                         /*required: true,  */
                         extension: "gif|png|jpg|jpeg|bmp"  
                     },
                     videoFile:{
                    	/* required:true,*/
                    	 extension:"rmvb|avi|wmv|mp4"
                     },
                     record_time:{
                    	 required:true
                     },
                     oldId:{
                    	 required:true
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


    };

    


   



    return {
        //main function to initiate the module
        init: function () {
            handleValidation1();
            handleValidation2();
        }

    };

}();

jQuery(document).ready(function() {
    FormValidation.init();
});