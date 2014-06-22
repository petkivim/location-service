$(document).ready( function() {
    $( "#slider" ).slider({
        range: "min",
        value: $( "#add_map_table_opacity_input" ).val(),
        min: 0,
        max: 255,
        slide: function( event, ui ) {
            if(ui.value > 0) {
                $( "#add_map_table_opacity_input" ).val( ui.value );
                $( "#slider_label" ).text( ui.value );
            } else {
                $( "#add_map_table_opacity_input" ).val( '' );
                $( "#slider_label" ).text( 'Use default' );
            }
        },
        create: function() {
            var value = $( "#add_map_table_opacity_input" ).val();
            if(value == '') {
                value = 'Use default';
            }
            $( "#slider_label" ).text( value );
        }
    });
});