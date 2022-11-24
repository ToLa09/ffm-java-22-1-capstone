import React, {ChangeEvent, useState} from 'react';
import {Alert, Box, Button, Snackbar, TextField} from "@mui/material";
import axios from "axios";
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";

function AddForm() {
    const [snackbarAddOpen, setSnackbarAddOpen] = useState<boolean>(false)
    const [snackbarErrorOpen, setSnackbarErrorOpen] = useState<boolean>(false)

    const [newDiagram, setNewDiagram] = useState<BpmnDiagramModel>({
        id:""
        ,name: ""
        ,businessKey: ""
        ,filename: ""
        ,version: 1
        ,calledProcesses: []
        ,commentText: ""
        ,commentAuthor: ""
    })

    const handleSubmit = (event: any) => {
        event.preventDefault()
        if(newDiagram.name==="" || newDiagram.businessKey==="" || newDiagram.filename==="" ){
            setSnackbarErrorOpen(true)
            return
        }
        axios.post("/api/bpmndiagrams",newDiagram)
            .then(() => {
                setNewDiagram({
                    id: ""
                    , name: ""
                    , businessKey: ""
                    , filename: ""
                    , version: 1
                    , calledProcesses: []
                    , commentText: ""
                    , commentAuthor: ""
                })
                setSnackbarAddOpen(true)
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
        <>
        <Box
            component="form"
            sx={{
                '& .MuiTextField-root': { m: 1, width: '25ch' },
            }}
            noValidate
            autoComplete="off"
        >
            <div>
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
            </div>
        </Box>
        <Box>
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
        </>
        // <form className="addForm" onSubmit={handleSubmit}>
        //     <div className="addFormInputWrapper"><TextField size="small" className="addFormInputField" variant="outlined" label="Name" value={name} onChange={e => setName(e.target.value)}/></div>
        //     <div className="addFormInputWrapper"><TextField size="small" className="addFormInputField" variant="outlined" label="Businesskey" value={businessKey} onChange={e => setBusinessKey(e.target.value)}/></div>
        //     <div className="addFormInputWrapper"><TextField size="small" className="addFormInputField" variant="outlined" label="XML-Filename" value={fileName} onChange={e => setFileName(e.target.value)}/></div>
        //     <div className="addFormInputWrapper"><TextField size="small" className="addFormInputField" variant="outlined" label="Comment" value={comment} onChange={e => setComment(e.target.value)}/></div>
        //     <Button variant="contained" color="primary" type="submit">Add</Button>
        // </form>
    );
}

export default AddForm;