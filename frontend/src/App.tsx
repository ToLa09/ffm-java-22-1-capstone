import React, {useEffect, useState} from 'react';
import './css/App.css';
import DiagramTable from "./components/OverviewPage/DiagramTable";
import AddForm from "./components/AddPage/AddForm";
import {AppBar, createTheme, IconButton, ThemeProvider, Toolbar, Typography} from "@mui/material";
import DiagramDetails from "./components/DetailsPage/DiagramDetails";
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import axios from "axios";
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import AccountTreeIcon from '@mui/icons-material/AccountTree';
import NavBar from "./components/NavBar";

function App() {

    const [detailedDiagram, setDetailedDiagram] = useState<BpmnDiagramModel>({
        id: ""
        , name: "-"
        , businessKey: "-"
        , filename: "-"
        , version: 1
        , comments: []
        , customDiagram: true
    })
    const [bpmnDiagrams, setBpmnDiagrams] = useState<BpmnDiagramModel[]>([])

    const fetchDiagrams = () => {
        axios.get("/api/bpmndiagrams/?onlylatestversions=true")
            .then(response => response.data)
            .catch(error => console.error("GET-Error: " + error))
            .then(setBpmnDiagrams)
    }

    useEffect(() => {
        fetchDiagrams()
    }, [])

    const theme = createTheme({
        palette: {
            primary: {
                main: '#fc5d0d',
            },
            secondary: {
                main: '#336571',
            },
            background: {
                default: '#f9ded4',
            },
        },
    })

    return (
        <ThemeProvider theme={theme}>
            <BrowserRouter>
                <header>
                    <AppBar position="sticky">
                        <Toolbar>
                            <IconButton size="large" edge="start" color="inherit" aria-label="logo">
                                <AccountTreeIcon fontSize="large"/>
                            </IconButton>
                            <Typography variant="h2" color="inherit">BPMN-Library</Typography>
                        </Toolbar>
                    </AppBar>
                </header>
                <main>
                    {/*<TabContext value={tab}>*/}
                    <NavBar
                        detailedDiagram={detailedDiagram}
                    />
                    <Routes>
                        <Route path="/" element={
                            <DiagramTable
                                setDetailedDiagram={setDetailedDiagram}
                                bpmnDiagrams={bpmnDiagrams}
                                fetchDiagrams={fetchDiagrams}
                            />
                        }/>
                        <Route path="/:id" element={
                            <DiagramDetails
                                detailedDiagram={detailedDiagram}
                                setDetailedDiagram={setDetailedDiagram}
                                fetchDiagrams={fetchDiagrams}
                                bpmnDiagrams={bpmnDiagrams}
                            />
                        }/>
                        <Route path="/add" element={
                            <AddForm fetchDiagrams={fetchDiagrams}/>
                        }/>
                    </Routes>
                    {/*<TabPanel value="Overview">*/}
                    {/*    <DiagramTable*/}
                    {/*        setTab={setTab}*/}
                    {/*        bpmnDiagrams={bpmnDiagrams}*/}
                    {/*        fetchDiagrams={fetchDiagrams}*/}
                    {/*        setDetailedDiagram={setDetailedDiagram}/>*/}
                    {/*</TabPanel>*/}
                    {/*<TabPanel value="Add">*/}
                    {/*    <AddForm fetchDiagrams={fetchDiagrams}/>*/}
                    {/*</TabPanel>*/}
                    {/*<TabPanel value="Details">*/}
                    {/*    <DiagramDetails*/}
                    {/*        setTab={setTab}*/}
                    {/*        detailedDiagram={detailedDiagram}*/}
                    {/*        setDetailedDiagram={setDetailedDiagram}*/}
                    {/*        fetchDiagrams={fetchDiagrams}*/}
                    {/*    />*/}
                    {/*</TabPanel>*/}
                    {/*</TabContext>*/}
                </main>
            </BrowserRouter>
        </ThemeProvider>
    );
}

export default App;
