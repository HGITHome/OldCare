var DateF = function () {

	 var handleDatePickers = function () {

	        if (jQuery().datepicker) {
	            $('.date-picker').datepicker({
	                rtl: App.isRTL(),
	                orientation: "left",
	                autoclose: true,
	           	 language: 'zh-CN',
	           	todayBtn:"linked",
	           	pickerPosition:"bottom-left"
	            });
	            //$('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
	        }
	        
	        $(".form_datetime").datetimepicker({
	        	 language: 'zh-CN',
	            autoclose: true,
	            isRTL: App.isRTL(),
	            format: "yyyy-mm-dd hh:ii",
	            pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left")
	        });

	        /* Workaround to restrict daterange past date select: http://stackoverflow.com/questions/11933173/how-to-restrict-the-selectable-date-ranges-in-bootstrap-datepicker */
	    }


    return {
        //main function to initiate the module
        init: function () {
        	handleDatePickers();
        }

    };

}();

jQuery(document).ready(function() {
	DateF.init();
});