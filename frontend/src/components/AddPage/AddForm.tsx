import React, {ChangeEvent, useState} from 'react';
import {Alert, Box, Button, Snackbar, TextField} from "@mui/material";
import axios from "axios";
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";

type AddFormProps = {
    fetchDiagrams: () => void
}

function AddForm(props: AddFormProps) {
    const [snackbarAddOpen, setSnackbarAddOpen] = useState(false)
    const [snackbarErrorOpen, setSnackbarErrorOpen] = useState(false)

    const [newDiagram, setNewDiagram] = useState<BpmnDiagramModel>({
        id: ""
        , name: ""
        , businessKey: ""
        , filename: ""
        , version: 1
        , comments: []
        , calledDiagrams: []
        , template: false
        , customDiagram: true
    })

    const handleSubmit = (event: any) => {
        event.preventDefault()
        if (!newDiagram.name || !newDiagram.businessKey || !newDiagram.filename) {
            setSnackbarErrorOpen(true)
            return
        }
        axios.post("/api/bpmndiagrams", newDiagram)
            .then(() => {
                setNewDiagram({
                    id: ""
                    , name: ""
                    , businessKey: ""
                    , filename: ""
                    , version: 1
                    , comments: []
                    , calledDiagrams: []
                    , template: false
                    , customDiagram: true
                })
                setSnackbarAddOpen(true)
                props.fetchDiagrams()
            })
            .catch(error => console.error(error))
    }

    function handleChange(event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        setNewDiagram({
            ...newDiagram,
            [event.target.name]: event.target.value
        })
    }

    return (
        <Box m={5}>
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
                    value={newDiagram.name}
                    onChange={handleChange}/>
                <TextField
                    variant="outlined"
                    label="Businesskey"
                    name="businessKey"
                    value={newDiagram.businessKey}
                    onChange={handleChange}/>
                <TextField
                    variant="outlined"
                    label="Filename"
                    name="filename"
                    value={newDiagram.filename}
                    onChange={handleChange}/>
                <TextField
                    variant="outlined"
                    label="Version"
                    name="version"
                    value={newDiagram.version}
                    onChange={handleChange}/>
            </Box>
            <Box sx={{m: 1}}>
                <Button variant="contained" color="primary" type="submit" onClick={handleSubmit}>Add</Button>
            </Box>
            <Snackbar
                open={snackbarAddOpen}
                autoHideDuration={3000}
                message="Diagram added!"
                onClose={() => setSnackbarAddOpen(false)}
                action={<IconButton onClick={() => setSnackbarAddOpen(false)}><CloseIcon/></IconButton>}
            />
            <Snackbar
                open={snackbarErrorOpen}
                autoHideDuration={3000}
                onClose={() => setSnackbarErrorOpen(false)}
                action={<IconButton onClick={() => setSnackbarErrorOpen(false)}><CloseIcon/></IconButton>}
            ><Alert severity="error">Inputfields must not be empty!</Alert></Snackbar>
        </Box>
    );
}

export default AddForm;
