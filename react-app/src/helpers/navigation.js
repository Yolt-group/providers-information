export const updateUrl = (selectAllToggled, selectedSiteIds, history) => {
    const urlParams = new URLSearchParams();
    if (selectAllToggled) {
        urlParams.append('all', true)
    } else {
        selectedSiteIds.forEach(element => {
            urlParams.append('sid', element)
        });
    }
    history.push({search: urlParams.toString()})
};