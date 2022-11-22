import React, {FormEvent, useState} from 'react';
import './css/App.css';
import DiagramTable from "./DiagramTable";
import axios from "axios";
import {Alert, Button, Snackbar, TextField} from "@mui/material";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";

function App() {
    const [name,setName] = useState<string>("")
    const [businessKey,setBusinessKey] = useState<string>("")
    const [fileName,setFileName] = useState<string>("")
    const [comment,setComment] = useState<string>("")
    const [snackbarAddOpen, setSnackbarAddOpen] = useState<boolean>(false)
    const [snackbarErrorOpen, setSnackbarErrorOpen] = useState<boolean>(false)

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        if(name==="" || businessKey==="" || fileName===""){
            setSnackbarErrorOpen(true)
            return
        }
        axios.post("/api/bpmndiagrams",{
                    "name": name,
                    "businessKey": businessKey,
                    "xmlFile": fileName,
                    "comment": comment
                })
            .then(() => {
                setName("")
                setBusinessKey("")
                setFileName("")
                setComment("")
                setSnackbarAddOpen(true)
            })
            .catch(error => console.error(error))
    }

  return (
    <>
        <header>
            <h1>BPMN-Library</h1>
            <form className="addForm" onSubmit={handleSubmit}>
                <div className="addFormInputWrapper"><TextField size="small" className="addFormInputField" variant="outlined" label="Name" value={name} onChange={e => setName(e.target.value)}/></div>
                <div className="addFormInputWrapper"><TextField size="small" className="addFormInputField" variant="outlined" label="Businesskey" value={businessKey} onChange={e => setBusinessKey(e.target.value)}/></div>
                <div className="addFormInputWrapper"><TextField size="small" className="addFormInputField" variant="outlined" label="XML-Filename" value={fileName} onChange={e => setFileName(e.target.value)}/></div>
                <div className="addFormInputWrapper"><TextField size="small" className="addFormInputField" variant="outlined" label="Comment" value={comment} onChange={e => setComment(e.target.value)}/></div>
                <Button variant="contained" color="primary" type="submit">Add</Button>
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
            </form>
        </header>
        <main>
            <DiagramTable/>
        </main>
    </>
  );
}

export default App;
