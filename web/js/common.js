/* jshint browser : true, jquery : true */
/**
 * Clears a select and set the default option
 * @author Miguelcldn
 * @param {object} select          The jQuery object
 * @param {string} [defaultOption] The text to show in the default option
 */
function clearSelect(select, defaultOption) {
    
    select.empty();
    
    defaultOption = defaultOption || "------------";
    
    select.append('<option value="none">' + defaultOption + '</option>');
    select.val("none");
}

/**
 * Sends the client to the root address
 * @author Miguelcldn
 */
function goToRoot() {
    window.location.href = "/ZenderozWeb";
}