import React, {ChangeEvent, Dispatch, SetStateAction, useEffect, useState} from 'react';
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import {Box, Button, Card, CardContent, Grid, Snackbar, TextField, Typography} from "@mui/material";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import axios from "axios";
import HistoryList from "./HistoryList";

type DiagramDetailsProps = {
    setTab: Dispatch<SetStateAction<string>>
    detailedDiagram: BpmnDiagramModel
    setDetailedDiagram: Dispatch<SetStateAction<BpmnDiagramModel>>
    fetchDiagrams: () => void
}

function DiagramDetails(props: DiagramDetailsProps) {

    const [snackbarOpen, setSnackbarOpen] = useState<boolean>(false)
    const [snackbarMessage, setSnackbarMessage] = useState<string>("")
    const [editMode, setEditMode] = useState<boolean>(false)
    const [updatedDiagram, setUpdatedDiagram] = useState<BpmnDiagramModel>(props.detailedDiagram)
    const [history, setHistory] = useState<BpmnDiagramModel[]>([])


    const handleUpdate = () => {
        axios.put("/api/bpmndiagrams/" + props.detailedDiagram.id, updatedDiagram)
            .then(() => {
                props.fetchDiagrams()
                if (updatedDiagram !== props.detailedDiagram) {
                    setSnackbarOpen(true)
                    setSnackbarMessage("Diagram updated!")
                    props.setDetailedDiagram(updatedDiagram)
                }
            })
            .catch(error => console.error(error))
        setEditMode(false)
    }

    function handleChange(event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        setUpdatedDiagram({
            ...updatedDiagram,
            [event.target.name]: event.target.value
        })
    }

    const fetchHistory = () => {
        axios.get("/api/bpmndiagrams/history/" + props.detailedDiagram.businessKey)
            .then(response => response.data)
            .then(setHistory)
    }

    useEffect(fetchHistory, [])

    return (
        <>
            <Grid container spacing={2}>
                <Grid item xs={12}><Button variant="outlined" color="secondary"
                                           onClick={() => props.setTab("Overview")}>Back to Overview</Button></Grid>
                <Grid item xs={6}>
                    <Card>
                        <CardContent>
                            <Grid container spacing={0}>
                                <Grid item xs={10}><Typography variant="h5"
                                                               color="secondary">Properties</Typography></Grid>
                                {editMode ?
                                    <Button variant="outlined" color="secondary" onClick={handleUpdate}>Update</Button>
                                    :
                                    <Grid item xs>
                                        {props.detailedDiagram.customDiagram ?
                                            <Button variant="outlined" color="secondary"
                                                    onClick={() => setEditMode(true)}>Edit</Button>
                                            :
                                            <Button variant="outlined" color="secondary" disabled>Edit</Button>
                                        }
                                    </Grid>
                                }
                            </Grid>
                            {editMode ?
                                <Box
                                    component="form"
                                    sx={{
                                        '& .MuiTextField-root': {m: 1, width: '25ch'},
                                    }}
                                    noValidate
                                    autoComplete="off"
                                >
                                    <TextField
                                        variant="outlined"
                                        label="Name"
                                        name="name"
                                        value={updatedDiagram.name}
                                        onChange={handleChange}/>
                                    <TextField
                                        variant="outlined"
                                        label="Businesskey"
                                        name="businessKey"
                                        value={updatedDiagram.businessKey}
                                        onChange={handleChange}/>
                                    <TextField
                                        variant="outlined"
                                        label="Filename"
                                        name="filename"
                                        value={updatedDiagram.filename}
                                        onChange={handleChange}/>
                                    <TextField
                                        variant="outlined"
                                        label="Version"
                                        name="version"
                                        value={updatedDiagram.version}
                                        onChange={handleChange}/>
                                </Box>
                                :
                                <>
                                    <Typography>Name: {props.detailedDiagram.name}</Typography>
                                    <Typography>Business Key: {props.detailedDiagram.businessKey}</Typography>
                                    <Typography>Filename: {props.detailedDiagram.filename}</Typography>
                                    <Typography>Latest Version: {props.detailedDiagram.version}</Typography>
                                </>
                            }
                        </CardContent>
                    </Card>
                </Grid>
                <Grid item xs={12}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5"
                                        color="secondary">History</Typography>
                            <HistoryList
                                history={history}
                                customDiagram={props.detailedDiagram.customDiagram}
                                setSnackbarOpen={setSnackbarOpen}
                                setSnackbarMessage={setSnackbarMessage}
                                fetchHistory={fetchHistory}
                                fetchDiagrams={props.fetchDiagrams}
                                latestDiagram={props.detailedDiagram}
                                setTab={props.setTab}
                            />
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>
            <Snackbar
                open={snackbarOpen}
                autoHideDuration={3000}
                message={snackbarMessage}
                onClose={() => setSnackbarOpen(false)}
                action={<IconButton onClick={() => setSnackbarOpen(false)}><CloseIcon/></IconButton>}
            />
        </>
    );
}

export default DiagramDetails;
