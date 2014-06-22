/* jQuery Link Subject Matters plugin 
 * Version: 1.0 
 * Date: 4.11.2012
 * 
 * Copyright (c) 2012 Petteri Kivimäki, The National Library of Finland
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 */

(function($){
    $.extend({
        AnimateAreas: function(settings){
            var config = {
                'animate': true,
                'maxArea': 0,
                'minArea': 0,
                'level': '',
                'icon': '',
                'iconPath': 'icons/marker.png',
                'iconWidth': 32,
                'iconHeight': 32,
                'delay': 200,
                'leaveIconVisible': false,
                'repeatCount': 2
            };
            
            var default_icons = {
                'arrow_red' : 1,
                'arrow_yellow' : 1,
                'arrow_orange' : 1,
                'arrow_green' : 1,
                'marker' : 1
            };
            
            if (settings){
                $.extend(config, settings);
            }
        
            /* Check if a particular icon has been selected */
            if(default_icons[config.icon] != undefined) {
                config.iconPath = 'icons/' + config.icon + '.png';
            }
            
            /* Loop through library, collection and shelf maps */
            return $('img.map[id^="map_"]').each(function(i,obj){
                /* Get the type of the map: library, collection, shelf */
                var arr_id = $(this).attr('id').split('_');
                var type = arr_id[1];
                
                if(config.level != '' && config.level != type) {
                    return true;
                }

                /* Create a new dev element, a wrapper for the map */
                var div = $('<div>');
                div.css({
                    'position':'relative',
                    'display':'inline-block'
                });                
                div.attr('id', 'map_' + type + '_wrapper');

                /* Move the map inside the wrapper div */
                $("#map_" + type).before(div);
                $("#map_" + type).remove().appendTo("#map_" + type + "_wrapper");

                /* Loop through all the area elements */
                $('map[name="' + type + '_coords"] area').each(function() {
                    /* Get the coordinates and place the into an array */
                    var arr = $(this).attr('coords').split(',');
                    
                    /* Calculate the size of the area */
                    var area = (arr[2]-arr[0]) * (arr[3]-arr[1]);
    
                    /* If the size is bigger than mx size, jump to next */
                    if(config.maxArea > 0 && area > config.maxArea) {
                        return true;
                    }

                    /* If the size is smaller than min size, jump to next */
                    if(config.minArea > 0 && area < config.minArea) {
                        return true;
                    }
                    
                    /* Calculate the coordinates of the center of the area */
                    var center_y = ((parseInt(arr[3]) - parseInt(arr[1])) / 2) + parseInt(arr[1]);
                    var center_x = ((parseInt(arr[2]) - parseInt(arr[0])) / 2) + parseInt(arr[0]);
    
                    /* Create img tag for the marker */
                    var marker = $('<img>');
                    marker.attr('class', 'marker_' + type);
                    marker.attr('src', config.iconPath);
                    marker.css({
                        'display':'none'
                    });
                    marker.css({
                        'position':'absolute',
                        'width':config.iconWidth,
                        'height':config.iconHeight,
                        'top':  center_y - config.iconHeight,
                        'left': center_x - (config.iconWidth / 2)
                    });
                    /* Add the marker to the document */
                    $("#map_" + type + "_wrapper").append(marker);
                });
                
                if(config.animate) {
                    /* Flash matkers */
                    for (var j = 0; j < config.repeatCount; j++) {
                        $('.marker_' + type).fadeIn('slow').delay(config.delay).fadeOut('slow');                       
                    } 
                    if(config.leaveIconVisible) {
                        $('.marker_' + type).fadeIn('slow');
                    }
                } else {
                    $('.marker_' + type).fadeIn('slow');
                }                
            });
        }
    });
})(jQuery);
