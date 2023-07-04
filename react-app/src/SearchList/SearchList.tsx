import React, { useEffect, useState } from "react";
import Form from 'react-bootstrap/Form'
import "../App.css";
import { fetchSearchList } from "../SearchList/action"
import { updateUrl } from "../helpers/navigation";
import { history } from '../store/configureStore';
import { useDispatch, useSelector } from "react-redux";
import { searchListSlice, selectSelections } from "./searchListSlice";

function SearchListItemSet(props: any) {
    const listItems = props.filteredList.map((el: any) => (
        <Form.Check
            key={el.siteId}
            type="checkbox" label={el.providerName}
            checked={el.selectedSite || props.selectAllChecked}
            onChange={() => {
                props.onListElementChange(el);
            }}
        />
    ));
    listItems.unshift(
        <Form.Check
            key="selectAll"
            type="checkbox" label={"Select all (" + props.filteredList.length + ")"}
            checked={props.selectAllChecked}
            onChange={() => {
                props.onSelectAllChange();
            }} />
    );
    return <div className="sidenav">
        <Form>
            <Form.Group>{listItems}</Form.Group>
        </Form>
    </div>;
}

const filterSiteList = (searchText: string, list: SimpleSite[]): SimpleSite[] => {
    if (searchText) {
        return list.filter((elem: SimpleSite) => {
            return elem.providerName.toLowerCase().includes(searchText.toLowerCase());
        });
    } else {
        return list;
    }
};

export const SearchList = () => {
    const { selectedSiteIds, selectAllToggled, data } = useSelector(selectSelections)
    const dispatch = useDispatch()

    const toggleAll = () => {
        dispatch(searchListSlice.actions.toggleAll());
    }

    const checkElement = (simpleSite: SimpleSite) => {
        dispatch(searchListSlice.actions.checkElement(simpleSite));
    }

    useEffect(() => {
        dispatch(fetchSearchList(0))
    }, [dispatch]);

    const updateUrlWithGivenHistoryAndLocation = () => {
        updateUrl(selectAllToggled, selectedSiteIds, history);
    };

    useEffect(updateUrlWithGivenHistoryAndLocation, [selectAllToggled, selectedSiteIds]);

    const [searchText, setSearchText] = useState("");
    const filteredList = Array.from(filterSiteList(searchText, data)).map(
        (elem: SimpleSite) => {
            return Object.assign({}, elem, {
                selectedSite: selectedSiteIds.includes(elem.siteId),
            });
        }
    );
    return (
        <div className="col-sm-2 leftpanel">
            <Form.Control
                className="form-control bd-search d-flex align-items-center searchbox"
                type="text"
                placeholder="Search"
                aria-label="Search"
                onChange={(e) => setSearchText(e.target.value)}
            />
            <SearchListItemSet
                selectAllChecked={selectAllToggled}
                filteredList={filteredList}
                onListElementChange={checkElement}
                onSelectAllChange={toggleAll}
            />
        </div>
    );
}