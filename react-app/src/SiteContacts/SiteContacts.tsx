import React, { useEffect } from "react";
import Table from 'react-bootstrap/Table'
import { SiteContactElement } from "./types";
import { ApplicationState } from "../store";
import { ThunkDispatch } from "redux-thunk";
import { AnyAction } from "redux";
import { fetchRequest } from "./action";
import { connect } from "react-redux";

type SiteContactListProps = {
    selectAllToggled: boolean
    selectedSiteIds: string[]
    contacts: SiteContactElement[]
};

interface propsFromDispatch {
    fetchRequest: () => any
}

function SiteContacts({ selectedSiteIds, selectAllToggled, contacts, fetchRequest }: SiteContactListProps & propsFromDispatch) {
    useEffect(() => {
        fetchRequest();
    }, [fetchRequest]);

    const filteredContacts = contacts.filter((el: any) => {
        return selectAllToggled || selectedSiteIds.includes(el.siteId);
    });

    const rowList = filteredContacts.map((el: any) => (
        <tr>
            <td>{el.providerName}</td>
            <td>{el.siteId}</td>
            <td>{el.standard}</td>
            <td>{el.contact}</td>
        </tr>
    ));
    return (
        <Table striped hover size="sm">
            <thead>
                <tr>
                    <th>Provider Name</th>
                    <th>Site ID</th>
                    <th>Standard</th>
                    <th>Contact</th>
                </tr>
            </thead>
            <tbody>{rowList}</tbody>
        </Table>
    );
}

const mapStateToProps = ({ searchList, siteContactElement }: ApplicationState) => ({
    data: searchList.data,
    selectAllToggled: searchList.selectAllToggled,
    selectedSiteIds: searchList.selectedSiteIds,
    contacts: siteContactElement.data
});

const mapDispatchToProps = (dispatch: ThunkDispatch<any, any, AnyAction>) => {
    return {
        fetchRequest: () => {
            dispatch(fetchRequest());
        }
    };
};


export default connect(mapStateToProps, mapDispatchToProps)(SiteContacts);