$(document).ready( function() {
    /* Get list of subject matters. */
    var options = $("#subjectmatters option");
    
    /* Go through all the subject matters*/
    options.each(function()
    {
        /* Add class attribute which value is the language 
         * of the subject matter. */
        $(this).attr('class', $('#subject-' + $(this).val()).text());
    });
    
    /* Array for languages. */
    var languages = new Array();
    
    /* Get list of languages. */
    $('span[id^="subject-"]').each(function() {
        languages.push($(this).text()); 
    });

    /* Remove duplicate entries. */
    languages = jQuery.unique(languages);

    /* G through the list of unique languges. */
    $.each(languages, function(index, language) {
        /* Get all the subject matters in this language. */
        var opts = $("#subjectmatters option[class='" + language + "']");
        /* Create optgroup element. */
        var optgroup = $('<optgroup/>');
        /* Language is the optgroup's label.*/
        optgroup.attr('label', language);
        /* Wrap all the subject matters in this language
         * inside the optgroup. */
        opts.wrapAll(optgroup);
    });
    
    /* Initialize mulsti select plugin. */
    $('select#subjectmatters').multiSelect();
    
    /* Hide all the values under optgroups. */
    $('.ms-optgroup').find('li.ms-elem-selectable').hide();
    
    /* Show/hide values under optgroup when the
     * label is clicked. */
    $('.ms-optgroup-label').click(function(){
        if ($(this).hasClass('collapse')){
            $(this).nextAll('li').hide();
            $(this).removeClass('collapse'); 
        } else {
            $(this).nextAll('li:not(.ms-selected)').show();
            $(this).addClass('collapse');
        }
    });
});
