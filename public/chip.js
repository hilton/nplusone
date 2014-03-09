$(document).ready( function() {

	$('span.glyphicon-question-sign').click(function() {
		$('#definition').remove();
		var $template = $('#definition_template');
		var $definition = $template.clone().attr('id', 'definition');
		$definition.find('span').text($(this).attr('title'));
		$definition.removeClass('hidden');
		$template.after($definition);
	});
});
