import React, {useEffect, useState} from 'react';
import {AppBar, Tab, Toolbar} from "@mui/material";
import TabList from "@mui/lab/TabList";
import {useLocation, useNavigate} from "react-router-dom";
import {BpmnDiagramModel} from "../model/BpmnDiagramModel";
import {TabContext} from "@mui/lab";

type NavBarProps = {
    detailedDiagram: BpmnDiagramModel
}

function NavBar(props: NavBarProps) {
    const navigate = useNavigate()
    const location = useLocation()

    const [tab, setTab] = useState<string>("Overview")

    const setTabByLocation = () => {
        if (location.pathname === "/") {
            setTab("Overview")
        }
        if (location.pathname === "/add") {
            setTab("Add")
        }
        if (location.pathname.match("^/.*.{39}$")) {
            setTab("Details")
        }
    }

    useEffect(setTabByLocation, [location.pathname])

    return (
        <AppBar position="sticky">
            <Toolbar>
                <TabContext value={tab}>
                    <TabList
                        textColor="inherit"
                        indicatorColor="secondary"
                        onChange={(event, newValue: string) => setTab(newValue)}
                        aria-label="tabs"
                    >
                        <Tab onClick={() => navigate("/")} value="Overview" label="Process Overview" color="primary"/>
                        <Tab onClick={() => navigate("/add")} value="Add" label="Add Process"/>
                        {props.detailedDiagram.id !== "" &&
                            <Tab onClick={() => navigate("/" + props.detailedDiagram.id)} value="Details"
                                 label="Process Details"/>
                        }
                    </TabList>
                </TabContext>
            </Toolbar>
        </AppBar>
    );
}

export default NavBar;