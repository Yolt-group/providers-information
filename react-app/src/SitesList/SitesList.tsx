import React, { useEffect } from "react";
import Table from 'react-bootstrap/Table'
import DropdownButton from 'react-bootstrap/DropdownButton'
import Dropdown from 'react-bootstrap/Dropdown'
import { createUrl } from './utils'
import { ApplicationState } from "../store";
import { AnyAction } from "redux";
import { ThunkDispatch } from "redux-thunk";
import { connect } from "react-redux";
import { fetchRequest } from "./action";
import { SiteListElement } from "./types";

type SiteRowListProps = {
    sitesList: SiteListElement[]
}

function SitesRowList({ sitesList }: SiteRowListProps) {
    const rowList = sitesList.map((el: SiteListElement) => (
        <tr>
            <td>{el.providerName}</td>
            <td>{el.siteId}</td>
            <td>{el.services.join(', ')}</td>
            <td>{el.availableInCountries.join(', ')}</td>
            <td>{el.standard}</td>
            <td>{el.supportedAccounts}</td>
            <td>
                <DropdownButton id="dropdown-item-button" title="Links">
                    <Dropdown.ItemText>Kibana</Dropdown.ItemText>
                    <Dropdown.Item href={createUrl("https://kibana.app-prd.yolt.io", "yolt-kubernetes-default", el.siteId, "app-prd")} target="_blank">APP-PRD</Dropdown.Item>
                    <Dropdown.Item href={createUrl("https://kibana.yfb-prd.yolt.io", "yolt-kubernetes-ycs", el.siteId, "yfb-prd")} target="_blank">YFB-PRD</Dropdown.Item>
                    <Dropdown.Item href={createUrl("https://kibana.yfb-ext-prd.yolt.io", "yolt-kubernetes-ycs", el.siteId, "yfb")} target="_blank">YFB-PRD-EXT</Dropdown.Item>
                </DropdownButton>
            </td>
        </tr>
    ));
    return (<tbody>{rowList}</tbody>);
};

type SiteListElementProps = {
    selectAllToggled: boolean,
    selectedSiteIds: string[],
    siteListElements: SiteListElement[]
};

interface propsFromDispatch {
    fetchRequest: () => any
}

function SitesList({ selectAllToggled, selectedSiteIds, siteListElements, fetchRequest }: SiteListElementProps & propsFromDispatch) {
    useEffect(() => {
        fetchRequest();
    }, [fetchRequest]);
    const filteredSitesByCountry: SiteListElement[] = siteListElements.filter((el: any) => {
        return selectAllToggled || selectedSiteIds.includes(el.siteId);
    });

    return (
        <Table striped hover size="sm">
            <thead>
                <tr>
                    <th>Provider Name</th>
                    <th>Site ID</th>
                    <th>Services</th>
                    <th>Countries</th>
                    <th>Standard</th>
                    <th>Supported Accounts</th>
                    <th>Links</th>
                </tr>
            </thead>
            <SitesRowList sitesList={filteredSitesByCountry} />
        </Table>
    );
};

const mapStateToProps = ({ searchList, siteList }: ApplicationState) => ({
    data: searchList.data,
    selectAllToggled: searchList.selectAllToggled,
    selectedSiteIds: searchList.selectedSiteIds,
    siteListElements: siteList.data
});

const mapDispatchToProps = (dispatch: ThunkDispatch<any, any, AnyAction>) => {
    return {
        fetchRequest: () => {
            dispatch(fetchRequest());
        }
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(SitesList);