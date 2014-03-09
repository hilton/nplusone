/*
* CHIP Prediction Rule calculator ©Copyright 2007 Peter Hilton.
* This work is licensed under a Creative Commons License: http://creativecommons.org/licenses/by-nc-sa/1.0/
* Any derivative work must link to http://www.marionsmits.net/chip-prediction-rule/
*/

var form;

function calculate(control) {
	form = document.chip;
	var score = 0;
	
	score += calculateCheckbox(form.act, 0.8);
	score += calculateCheckbox(form.fracture, 2.3);
	score += calculateCheckbox(form.contusion, 0.6);
	score += calculateCheckbox(form.vomit, 0.8);
	score += calculateCheckbox(form.loc, 0.6);
	score += calculateCheckbox(form.seizure, 0.8);
	score += calculateCheckbox(form.deficit, 0.4);
	score += calculateCheckbox(form.memory, 0.4);
	
	score += calculateAge();
	score += calculateGcs0();
	score += calculateGcs1();
	score += calculateMechanism();
	score += calculatePta();
	
	var scoreValue = document.getElementById("scoreValue");
	scoreValue.innerHTML = new Number(score).toFixed(1);
	
	scoreValue.style.color = score >= 1.1 ? "#c00" : "auto";
	
	document.getElementById("probabilityValue").innerHTML = calculateProbability(score);
}

function calculateCheckbox(checkbox, value) {
	return checkbox.checked ? value : 0;
}

function calculateAge() {
	var age = form.age.value;
	var score = new Number((age - 16) * 0.02).toFixed(1);
	return score > 0 ? new Number(score) : 0;
}

function calculateGcs0() {
	switch(radioButtonValue(form.gcs0)) {
		case "13":
			return 1.3;
		case "14":
			return 0.7;
		default:
			return 0;
	}
}

function calculateGcs1() {
	var gcs0 = radioButtonValue(form.gcs0);
	var gcs1 = form.gcs1.value;
	var score = new Number((gcs0 - gcs1) * 0.3).toFixed(1);
	return (gcs1 >= 3 && gcs1 <= 15) ? new Number(score) : 0;
}

function calculateMechanism() {
	switch(radioButtonValue(form.mechanism)) {
		case "fall":
			return 0.5;
		case "cyclist":
			return 1.1;
		case "ejected":
			return 0.8;
		default:
			return 0;
	}
}

function calculatePta() {
	switch(radioButtonValue(form.pta)) {
		case "2":
			return 0.4;
		case "4":
			return 0.6;
		default:
			return 0;
	}
}

function calculateProbability(score) {
	var prior;
	switch (form.prior.value) {
		case "2.5":
			prior = -1.2;
			break;
		case "5":
			prior = -0.4;
			break;
		case "7.5":
			prior = 0.0;
			break;
		case "10":
			prior = 0.3;
			break;
		case "12.5":
			prior = 0.6;
			break;
		case "15":
			prior = 0.8;
			break;
		default:
			alert("default case: " + new Number(form.prior.value));
	}
	
	var probability = 1 / (1 + Math.exp(-1*(-4.6 + score + prior)));
	return new Number(probability*100).toFixed(0) + "%";
}

// Return the value of the checked radio button in the given group.
function radioButtonValue(button) {
	for (var i = 0; i < button.length; i++) {
		if (button[i].checked) {
			return button[i].value;
		}
	}
	return "";
}


$(document).ready( function() {
	calculate();

	$('span.glyphicon-question-sign').click(function() {
		$('#definition').remove();
		var $template = $('#definition_template');
		var $definition = $template.clone().attr('id', 'definition');
		$definition.find('span').text($(this).attr('title'));
		$definition.removeClass('hidden');
		$template.after($definition);
	});
});
