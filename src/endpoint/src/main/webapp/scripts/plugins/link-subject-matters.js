/* jQuery Link Subject Matters plugin 
 * Version: 1.0 
 * Date: 1.4.2012
 * 
 * Copyright (c) 2012 Petteri Kivimäki, The National Library of Finland
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 */

(function($){
    var collection_codes = {};
    $.fn.LinkSubjectMatters = function(settings){
        var config = {
            'owner': '',
            'target': '',
            'hideIfEmpty' : true
        };

        if (settings){
            $.extend(config, settings);
        }
        
        return this.each(function(i,obj){
            $(this).hide();
            var lang = $(this).attr('lang');
            var result = "<ul>\n";
            var results = {};
 
            $('span[class^="subject_matter_' + config.target + '"]').each(function(){
                if(results[$(this).text()] == undefined) {
                    results[$(this).text()] = search($(this).text(), config.owner);
                }
            });

            var array = get_unique_list(results);
            array.sort();
                     
            $.each(array, function(i, location) {
                var link = "LocationHandler?callno=" + location
                + "&status=0&lang=" + lang + "&owner=" + config.owner;
                result += "<li><a href='" + link + "'>" + 
                location.replace(/\&collection=[\w\-_\+]+/, '')
                + "</a></li>\n";
            });
            result += "</ul>\n";
            result = $(this).html() + result;
            $(this).html(result);
            if(array.length > 1 || !config.hideIfEmpty) {
                $(this).show();
            }            
        });
    };

    function search(search_str, owner) {
        var results = {};
        $.ajax({
            async: false,
            url: "Exporter",
            data: "owner=" + owner + "&type=subject&children=no&search="
            + search_str + "&position=match",
            dataType: "xml",
            success: function(xml){
                $(xml).find('location').each(function(){
                    results[$(this).find('locationid').text()] = $(this).find('callnumber').text();
                    if($(this).find('collectioncode').text().length > 0) {
                        collection_codes[$(this).find('locationid').text()] = $(this).find('collectioncode').text();
                    }
                });
            }
        });
        return results;
    }

    function get_unique_list(list) {
        var exist = {}
        var result = new Array();
        $.each(list, function(subject_matter, locations) {
            $.each(locations, function(id, location) {
                if(exist[id] == undefined) {
                    if(collection_codes[id] != undefined) {
                        location += "&collection=" + collection_codes[id];
                    }
                    exist[id] = location;
                    result.push(location);
                }
            });
        });
        return result;
    }
})(jQuery);