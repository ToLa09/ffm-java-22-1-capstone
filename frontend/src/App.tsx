import React, {useCallback, useEffect, useState} from 'react';
import './css/App.css';
import DiagramTable from "./components/OverviewPage/DiagramTable";
import AddForm from "./components/AddPage/AddForm";
import {AppBar, createTheme, ThemeProvider, Toolbar, Typography} from "@mui/material";
import DiagramDetails from "./components/DetailsPage/DiagramDetails";
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import axios from "axios";
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import AccountTreeIcon from '@mui/icons-material/AccountTree';
import NavBar from "./components/NavBar";

function App() {
    const [bpmnDiagrams, setBpmnDiagrams] = useState<BpmnDiagramModel[]>([])
    const [detailedDiagramId, setDetailedDiagramId] = useState<string>("")

    const fetchDiagrams = useCallback(() => {
        axios.get("/api/bpmndiagrams/?onlylatestversions=true")
            .then(response => response.data)
            .catch(error => console.error("GET-Error: " + error))
            .then(setBpmnDiagrams)
    }, [])

    useEffect(() => {
        fetchDiagrams()
    }, [fetchDiagrams])

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
            <BrowserRouter>
                <header>
                    <AppBar position="sticky">
                        <Toolbar>
                            <AccountTreeIcon fontSize="large"/>
                            <Typography variant="h2" color="inherit">BPMN-Library</Typography>
                        </Toolbar>
                    </AppBar>
                </header>
                <main>
                    <NavBar
                        detailedDiagramId={detailedDiagramId}
                    />
                    <Routes>
                        <Route path="/" element={
                            <DiagramTable
                                setDetailedDiagramId={setDetailedDiagramId}
                                bpmnDiagrams={bpmnDiagrams}
                                fetchDiagrams={fetchDiagrams}
                            />
                        }/>
                        <Route path="/:id" element={
                            <DiagramDetails
                                detailedDiagramId={detailedDiagramId}
                                setDetailedDiagramId={setDetailedDiagramId}
                                fetchDiagrams={fetchDiagrams}
                                bpmnDiagrams={bpmnDiagrams}
                            />
                        }/>
                        <Route path="/add" element={
                            <AddForm fetchDiagrams={fetchDiagrams}/>
                        }/>
                    </Routes>
                </main>
            </BrowserRouter>
        </ThemeProvider>
    );
}

export default App;
