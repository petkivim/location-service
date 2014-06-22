$(document).ready( function() {
    $(':input:visible:enabled:first').focus();
    
    $( "#slider" ).slider({
        range: "min",
        value: $( "#userinfo_input_opacity" ).val(),
        min: 0,
        max: 255,
        slide: function( event, ui ) {
            $( "#userinfo_input_opacity" ).val( ui.value );             
            $( "#slider_label" ).text( ui.value );            
        },
        create: function() {
            $( "#slider_label" ).text( $( "#userinfo_input_opacity" ).val() );
        }
    });
});