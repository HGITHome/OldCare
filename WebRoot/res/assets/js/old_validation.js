var FormValidation = function () {

    // basic validation
    var handleValidation1 = function () {
        // for more info visit the official plugin documentation: 
        // http://docs.jquery.com/Plugins/Validation

        var form = $('#submit_form');
        var error = $('.alert-danger', form);
        var success = $('.alert-success', form);

        form.validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block help-block-error', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            rules: {
                //basic
                username: {
                    minlength: 2,
                    required: true
                },
                age: {
                    required: true
                },
                sex: {
                    required: true
                },
                religion: {
                    required: true
                },

                diploma: {
                    required: true
                },
                marriage: {
                    required: true
                },
                'living[]': {
                    required: true,
                    minlength: 1
                },

                income: {
                    required: true,
                    digits: true
                },
                'payways[]': {
                    required: true,
                    minlength: 1
                },
                'incomeways[]': {
                    required: true,
                    minlength: 1
                },
                oafish: {
                    required: true
                },
                'insanity[]': {
                    required: true,
                    minlength: 1
                },
                'illness[]': {
                    required: true,
                    minlength: 1
                },
                'accident[]': {
                    required: true,
                    minlength: 1
                },
                
                town: {
                    required: true

                },
                photo: {
                    required: true,
                    extension: "gif|png|jpg|jpeg|bmp"
                }


            },

            messages: { // custom messages for radio buttons and checkboxes
                'payways[]': {
                    required: "至少选择一项",
                    minlength: jQuery.validator.format("Please select at least one option")
                },
                'living[]': {
                    required: "至少选择一项",
                    minlength: jQuery.validator.format("Please select at least one option")
                },
                'incomeways[]': {
                    required: "至少选择一项",
                    minlength: jQuery.validator.format("Please select at least one option")
                },
                'insanity[]': {
                    required: "至少选择一项",
                    minlength: jQuery.validator.format("Please select at least one option")
                },
                'illness[]': {
                    required: "至少选择一项",
                    minlength: jQuery.validator.format("Please select at least one option")
                },
                'accident[]': {
                    required: "至少选择一项",
                    minlength: jQuery.validator.format("Please select at least one option")
                },
                
                town: {
                    required: "镇区未选中"

                },
                photo: {
                    required: "上传文件不允许为空",
                    extension: "文件类型错误，请上传gif,bmp,png,jpg,jpeg文件"
                }
            },

            errorPlacement: function (error, element) { // render error placement for each input type
                if (element.attr("name") == "sex") { // for uniform radio buttons, insert the after the given container
                    error.insertAfter("#form_sex_error");
                }
                else if (element.attr("name") == "photo") {
                    error.insertAfter("#form_file_error");
                }
                else if (element.attr("name") == "religion") {
                    error.insertAfter("#form_religion_error");
                }
                else if (element.attr("name") == "diploma") {
                    error.insertAfter("#form_diploma_error");
                }
                else if (element.attr("name") == "marriage") {
                    error.insertAfter("#form_marriage_error");
                }
                else if (element.attr("name") == "town") {
                    error.insertAfter("#form_town_error");
                }
                else if (element.attr("name") == "oafish") {
                    error.insertAfter("#form_oafish_error");
                }

                else if (element.attr("name") == "payways[]") { // for uniform checkboxes, insert the after the given container
                    error.insertAfter("#form_payways_error");
                }
                else if (element.attr("name") == "living[]") {
                    error.insertAfter("#form_living_error");
                }
                else if (element.attr("name") == "incomeways[]") {
                    error.insertAfter("#form_incomeways_error");
                }
                else if (element.attr("name") == "illness[]") {
                    error.insertAfter("#form_illness_error");
                }
                else if (element.attr("name") == "accident[]") {
                    error.insertAfter("#form_accident_error");
                }
                else if (element.attr("name") == "insanity[]") {
                    error.insertAfter("#form_insanity_error");
                }
                else {
                    error.insertAfter(element); // for other inputs, just perform default behavior
                }
            },


            invalidHandler: function (event, validator) { //display error alert on form submit
                success.hide();
                error.show();
                App.scrollTo(error, -200);
            },

            highlight: function (element) { // hightlight error inputs
                $(element)
                    .closest('.form-group').removeClass('has-success').addClass('has-error'); // set error class to the control group
            },

            unhighlight: function (element) { // revert the change done by hightlight
                $(element)
                    .closest('.form-group').removeClass('has-error'); // set error class to the control group
            },

            success: function (label) {
                if (label.attr("for") == "sex" || label.attr("for") == "payways[]") { // for checkboxes and radio buttons, no need to show OK icon
                    label
                        .closest('.form-group').removeClass('has-error').addClass('has-success');
                    label.remove(); // remove error label here
                } else { // display success icon for other inputs
                    label
                        .addClass('valid') // mark the current input as valid and display OK icon
                        .closest('.form-group').removeClass('has-error').addClass('has-success'); // set success class to the control group
                }
            },
            submitHandler: function (form) {
                success.show();
                error.hide();
                form[0].submit();
                //add here some ajax code to submit your form or just call form.submit() if you want to submit the form without ajax
            }
        });

        var displayConfirm = function () {
            var record_date = $("#age").val();
            //alert(record_date);
            $("#ageInfo").val(record_date);
            $('#tab4 .form-control-static', form).each(function () {
                var input = $('[name="' + $(this).attr("data-display") + '"]', form);
                if (input.is(":radio")) {
                    input = $('[name="' + $(this).attr("data-display") + '"]:checked', form);
                }

                if (input.is(":text") || input.is("textarea")) {
                    $(this).html(input.val());
                }
                else if (input.is("select")) {
                    $(this).html(input.find('option:selected').text());
                }
                else if (input.is(":radio") && input.is(":checked")) {
                    $(this).html(input.attr("data-title"));
                }
                else if ($(this).attr("data-display") == 'payways[]') {
                    var payment = [];
                    $('[name="payways[]"]:checked', form).each(function () {
                        payment.push($(this).attr('data-title'));
                    });
                    if ($('#checkbox3_8').is(':checked') && $("#pay_others").val() != null) {
                        payment.push($("#pay_others").val());
                    }
                    $(this).html(payment.join("<br>"));
                }
                else if ($(this).attr("data-display") == "living[]") {
                    var livings = [];
                    $('[name="living[]"]:checked', form).each(function () {
                        livings.push($(this).attr('data-title'));
                    });
                    $(this).html(livings.join("<br>"));
                }
                else if ($(this).attr("data-display") == "incomeways[]") {
                    var incomeWays = [];
                    $('[name="incomeways[]"]:checked', form).each(function () {
                        incomeWays.push($(this).attr('data-title'));
                    });
                    if ($('#checkbox2_4').is(':checked') && $("#income_others").val() != null) {
                        incomeWays.push($("#income_others").val());
                    }
                    $(this).html(incomeWays.join("<br>"));
                }
                else if ($(this).attr("data-display") == "accident[]") {
                    var accident = [];
                    $('[name="accident[]"]:checked', form).each(function () {
                        accident.push($(this).attr('data-title'));
                    });
                    if ($('#checkbox6_17').is(':checked') && $("#accident_others").val() != null) {
                        accident.push($("#accident_others").val());
                    }
                    $(this).html(accident.join("<br>"));
                }

                else if ($(this).attr("data-display") == "insanity[]") {
                    var insanity = [];
                    $('[name="insanity[]"]:checked', form).each(function () {
                        insanity.push($(this).attr('data-title'));
                    });
                    if ($('#checkbox4_8').is(':checked') && $("#insanity_others").val() != null) {
                        insanity.push($("#insanity_others").val());
                    }
                    $(this).html(insanity.join("<br>"));
                }

                else if ($(this).attr("data-display") == "illness[]") {
                    var illness = [];
                    $('[name="illness[]"]:checked', form).each(function () {
                        illness.push($(this).attr('data-title'));
                    });
                    if ($('#checkbox5_13').is(':checked') && $("#illness_others").val() != null) {
                        illness.push($("#illness_others").val());
                    }
                    $(this).html(illness.join("<br>"));
                }

            });
        };

        var handleTitle = function (tab, navigation, index) {
            var total = navigation.find('li').length;
            var current = index + 1;
            // set wizard title
            $('.step-title', $('#form_wizard_1')).text('Step ' + (index + 1) + ' of ' + total);
            // set done steps
            jQuery('li', $('#form_wizard_1')).removeClass("done");
            var li_list = navigation.find('li');
            for (var i = 0; i < index; i++) {
                jQuery(li_list[i]).addClass("done");
            }

            if (current == 1) {
                $('#form_wizard_1').find('.button-previous').hide();
            } else {
                $('#form_wizard_1').find('.button-previous').show();
            }

            if (current >= total) {
                $('#form_wizard_1').find('.button-next').hide();
                $('#form_wizard_1').find('.button-submit').show();
                displayConfirm();
            } else {
                $('#form_wizard_1').find('.button-next').show();
                $('#form_wizard_1').find('.button-submit').hide();
            }
            App.scrollTo($('.page-title'));
        };

        // default form wizard
        $('#form_wizard_1').bootstrapWizard({
            'nextSelector': '.button-next',
            'previousSelector': '.button-previous',
            onTabClick: function (tab, navigation, index, clickedIndex) {
                return false;

                success.hide();
                error.hide();
                if (form.valid() == false) {
                    return false;
                }

                handleTitle(tab, navigation, clickedIndex);
            },
            onNext: function (tab, navigation, index) {
                success.hide();
                error.hide();

                if (form.valid() == false) {
                    return false;
                }

                handleTitle(tab, navigation, index);
            },
            onPrevious: function (tab, navigation, index) {
                success.hide();
                error.hide();

                handleTitle(tab, navigation, index);
            },
            onTabShow: function (tab, navigation, index) {
                var total = navigation.find('li').length;
                var current = index + 1;
                var $percent = (current / total) * 100;
                $('#form_wizard_1').find('.progress-bar').css({
                    width: $percent + '%'
                });
            }
        });

        $('#form_wizard_1').find('.button-previous').hide();
        $('#form_wizard_1 .button-submit').click(function () {
        }).hide();

    };
    
    
   
    


    return {
        //main function to initiate the module
        init: function () {

            if (!jQuery().bootstrapWizard) {
                return;
            }

            function format(state) {
                if (!state.id) return state.text; // optgroup
                return "<img class='flag' src='../../assets/global/img/flags/" + state.id.toLowerCase() + ".png'/>&nbsp;&nbsp;" + state.text;
            }

            handleValidation1();
            
        }

    };

}();

jQuery(document).ready(function () {

    $('#checkbox2_4').click(function () {
        if ($('#checkbox2_4').is(':checked')) {
            // do something
            $("#income_others").attr("disabled", false);
            /*alert(123);
             alert("其他="+$("#income_others").val());*/
            $("#income_others").attr("data-title", "其他补贴:" + $("#income_others").val());
        }
        else {

            $("#income_others").attr("disabled", true);
        }
    });

    $('#checkbox3_8').click(function () {
        if ($('#checkbox3_8').is(':checked')) {
            // do something
            $("#pay_others").attr("disabled", false);
            $("#pay_others").attr("data-title", "其他支付方式:" + $("#pay_others").val());
        }
        else {

            $("#pay_others").attr("disabled", true);
        }
    });

    $('#checkbox4_8').click(function () {
        if ($('#checkbox4_8').is(':checked')) {
            // do something
            $("#insanity_others").attr("disabled", false);
            $("#insanity_others").attr("data-title", "其他精神病:" + $("#insanity_others").val());
        }
        else {

            $("#insanity_others").attr("disabled", true);
        }
    });

    $('#checkbox5_13').click(function () {
        if ($('#checkbox5_13').is(':checked')) {
            // do something

            $("#illness_others").attr("disabled", false);
            $("#illness_others").attr("data-title", "其他病史:" + $("#illness_others").val());
        }
        else {

            $("#illness_others").attr("disabled", true);
        }
    });

    $('#checkbox6_17').click(function () {
        if ($('#checkbox6_17').is(':checked')) {
            // do something
            $("#accident_others").attr("disabled", false);
            $("#accident_others").attr("data-title", "其他意外:" + $("#accident_others").val());
        }
        else {

            $("#accident_others").attr("disabled", true);
        }
    });
    FormValidation.init();
});