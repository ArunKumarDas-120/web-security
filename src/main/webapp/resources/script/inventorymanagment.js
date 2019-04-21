$(document).ready(function() {
	/* Document ready start */
	
	/* Action Menu and tab */	
$('button.menu').click( function() {
	var menuid = $(this).attr('id');
	toggleActive($('button.menu') , menuid ,'active' , 'passive');
	toggleActive($('div.menu-tabcontent')  , menuid ,'open' , 'close');
});

	$('button.action').click( function(){
		var actionid  = $(this).attr('id');
		var parent  = $($(this).parent('div')).parent('div');
		var actionContent = $(parent).children('div.action-menu-tabcontent');
		toggleActive( $($(this).parent('div')).children('button.action') ,actionid,'active' , 'passive');
		toggleActive(actionContent , actionid ,'open' , 'close');
	});
	
	function toggleActive(ele , id, activeClass , passiveClass){
		$.each($(ele) , (index, element) => {
			if($(element).attr('id') == id && $(element).hasClass(passiveClass)){
			$(element).removeClass(passiveClass);
			$(element).addClass(activeClass);
		}else if($(element).hasClass(activeClass) && $(element).attr('id') != id){
			$(element).removeClass(activeClass);
			$(element).addClass(passiveClass);
		}
		});
	}
	/* Action Menu and tab End */
	
	/* Document ready end */
});
function add(action,formId){
	$.ajax({
		url : "/category/add",
		data :  $('#'+formId).serialize(),
		type : "POST",
		cache : false,
		success : function(response) {
			if(response.hasOwnProperty('Error')){
				popSnackBar(response.Error,'Red');
			}else{
				popSnackBar(response.Success,'Green');
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			  console.log(jqXHR.status);
			  console.log(textStatus);
			  console.log(errorThrown);
		}
	})
}