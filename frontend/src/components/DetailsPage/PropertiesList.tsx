import React, {ChangeEvent, Dispatch, SetStateAction, useState} from 'react';
import {Box, Button, Card, CardContent, Grid, TextField, Typography} from "@mui/material";
import axios from "axios";
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";

type PropertiesListProps = {
    detailedDiagram: BpmnDiagramModel
    fetchDiagrams: () => void
    setSnackbarOpen: Dispatch<SetStateAction<boolean>>
    setSnackbarMessage: Dispatch<SetStateAction<string>>
    setDetailedDiagram: Dispatch<SetStateAction<BpmnDiagramModel>>
}

function PropertiesList(props: PropertiesListProps) {
    const [editMode, setEditMode] = useState<boolean>(false)
    const [updatedDiagram, setUpdatedDiagram] = useState<BpmnDiagramModel>(props.detailedDiagram)

    const handleUpdate = () => {
        axios.put("/api/bpmndiagrams/" + props.detailedDiagram.id, updatedDiagram)
            .then(() => {
                props.fetchDiagrams()
                if (updatedDiagram !== props.detailedDiagram) {
                    props.setSnackbarOpen(true)
                    props.setSnackbarMessage("Diagram updated!")
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

    return (
        <Card>
            <CardContent>
                <Grid container spacing={0}>
                    <Grid item xs><Typography variant="h5"
                                              color="secondary">Properties</Typography></Grid>
                    {editMode ?
                        <Button variant="outlined" color="secondary" onClick={handleUpdate}>Update Properties</Button>
                        :
                        <Grid item xs="auto">
                            {props.detailedDiagram.customDiagram ?
                                <Button variant="outlined" color="secondary"
                                        onClick={() => setEditMode(true)}>Edit Properties</Button>
                                :
                                <Button variant="outlined" color="secondary" disabled>Edit Properties</Button>
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
    );
}

export default PropertiesList;