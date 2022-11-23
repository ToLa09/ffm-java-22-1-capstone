import React, {useEffect, useState} from 'react';
import './css/App.css';
import DiagramTable from "./DiagramTable";
import AddForm from "./AddForm";
import {AppBar, createTheme, Tab, ThemeProvider, Toolbar, Typography} from "@mui/material";
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
import DiagramDetails from "./DiagramDetails";
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import axios from "axios";

function App() {
    const [tab, setTab] = useState<string>("Overview")
    const [detailedDiagram, setDetailedDiagram] = useState<BpmnDiagramModel>({
        id:""
        ,name: ""
        ,businessKey: ""
        ,filename: ""
        ,version: NaN
        ,calledProcesses: []
        ,commentText: ""
        ,commentAuthor: ""
    })
    const [bpmnDiagrams, setBpmnDiagrams] = useState<BpmnDiagramModel[]>([])

    const fetchDiagrams = () => {
        axios.get("/api/bpmndiagrams")
            .then(response => response.data)
            .catch(error => console.error("GET-Error: " + error))
            .then(setBpmnDiagrams)
    }

    useEffect(() => {
        fetchDiagrams()
    },[])

    const theme = createTheme({
        palette: {
            primary: {
                main: '#fc5d0d',
            },
            secondary: {
                main: '#336571',
            },
        },
    })

  return (
    <ThemeProvider theme={theme}>
        <header>
            <AppBar position="sticky">
                <Typography variant="h2" color="inherit">BPMN-Library</Typography>
            </AppBar>
        </header>
        <main>
            <TabContext value={tab}>
                <AppBar position="sticky">
                    <Toolbar >
                        <TabList
                            textColor="inherit"
                            indicatorColor="secondary"
                            onChange={ (event, newValue: string) => setTab(newValue)}
                            aria-label="tabs"
                        >
                            <Tab value="Overview" label="Process Overview" color="primary"/>
                            <Tab value="Add" label="Add Process" />
                            <Tab value="Details" label="Process Details" />
                        </TabList>
                    </Toolbar>
                </AppBar>
                <TabPanel value="Overview">
                    <DiagramTable
                        setTab={setTab}
                        bpmnDiagrams={bpmnDiagrams}
                        fetchDiagrams={fetchDiagrams}
                        setDetailedDiagram={setDetailedDiagram}/>
                </TabPanel>
                <TabPanel value="Add">
                    <AddForm/>
                </TabPanel>
                <TabPanel value="Details">
                    <DiagramDetails
                        setTab={setTab}
                        detailedDiagram={detailedDiagram}
                        fetchDiagrams={fetchDiagrams}
                    />
                </TabPanel>
            </TabContext>
        </main>
    </ThemeProvider>
  );
}

export default App;
