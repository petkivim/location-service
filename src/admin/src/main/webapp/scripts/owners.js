$(document).ready( function() {
    $( "#slider" ).slider({
        range: "min",
        value: $( "#add_owner_table_opacity_input" ).val(),
        min: 0,
        max: 255,
        slide: function( event, ui ) {
            $( "#add_owner_table_opacity_input" ).val( ui.value );
            $( "#slider_label" ).text( ui.value );
        },
        create: function() {
            $( "#slider_label" ).text( $( "#add_owner_table_opacity_input" ).val() );
        }
    });

    $('.add_exception').click(function() {
        var id = $(this).parents('table').find('tr').last().find('td input').attr('id');
        var id_new = parseInt(id.replace(/\w+?([0-9]+).+/g, '$1'));
        id_new++;

        var tr = $(this).parents('table').find('tr').last().clone(true);
        tr.find('td input').each(function() {
            if($(this).attr('id') != undefined) {
                var attr_id = $(this).attr('id').replace(/(\w+?)([0-9]+)(.+)/g, '$1' + id_new + '$3');
                $(this).attr('id', attr_id);
            }
            var attr_name = $(this).attr('name').replace(/(\w+\[)([0-9]+)(\].+)/g, '$1' + id_new + '$3');
            $(this).attr('name', attr_name);           
            $(this).attr('value', '');
            $(this).val('');
        });
        tr.find('td input[type="checkbox"]').attr('value', 'true');
        tr.find('td input[type="checkbox"]').attr('checked', 'checked');
        tr.find('td input[type="hidden"]').attr('value', 'on');

        $(this).parents('table').find('tr').last().after(tr);
    });

    $('[class="icon-delete"]').on("click", function() {
        $(this).parent().closest('tr').find('td input').attr('value', '');
        $(this).parent().closest('tr').find('td input[type="checkbox"]').attr('value', 'false');
        $(this).parent().closest('tr').find('td input[type="checkbox"]').removeAttr('checked');
    });
});