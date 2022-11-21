import React, {useState} from 'react';
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import axios from "axios";
import './css/DiagramLine.css';
import TableCell from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import IconButton from "@mui/material/IconButton";
import EditIcon from "@mui/icons-material/Edit";
import CheckIcon from '@mui/icons-material/Check';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';

export type Props = {
    diagram: BpmnDiagramModel
    fetchDiagrams: () => void
}

export default function DiagramTableRow(props: Props) {

    const[editMode, setEditMode] = useState<boolean>(false)
    const [name,setName] = useState<string>(props.diagram.name)
    const [businessKey,setBusinessKey] = useState<string>(props.diagram.businessKey)
    const [xmlFile,setXmlFile] = useState<string>(props.diagram.xmlFile)
    const [comment,setComment] = useState<string>(props.diagram.comment)

    const handleUpdate = () => {
        axios.put("/api/bpmndiagrams/"+props.diagram.id, {
            "id": props.diagram.id,
            "name": name,
            "businessKey": businessKey,
            "xmlFile": xmlFile,
            "comment":comment
        })
            .then(props.fetchDiagrams)
            .catch(error => console.error(error))
    }

    const handleDuplicate = (diagram: BpmnDiagramModel) => {
        axios.post("/api/bpmndiagrams", {
                "name": diagram.name,
                "businessKey": diagram.businessKey,
                "xmlFile": diagram.xmlFile,
                "comment": "duplicated"
            })
            .then(props.fetchDiagrams)
            .catch(error => console.error(error))
    }

    const handleDelete = (id: string) => {
        axios.delete("/api/bpmndiagrams/"+id)
            .then(props.fetchDiagrams)
            .catch(error => console.error(error))
    }

    return (
        <>
        <TableRow
            key={props.diagram.id}
            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
        >
            { editMode ?
                <>
                    <TableCell component="th" scope="row"><input placeholder={"name"} value={name} onChange={(event) => setName(event.target.value)}/></TableCell>
                    <TableCell align="right"><input placeholder={"businessKey"} value={businessKey} onChange={(event) => setBusinessKey(event.target.value)}/></TableCell>
                    <TableCell align="right"><input placeholder={"xmlFile"} value={xmlFile} onChange={(event) => setXmlFile(event.target.value)}/></TableCell>
                    <TableCell align="right"><input placeholder={"comment"} value={comment} onChange={(event) => setComment(event.target.value)}/></TableCell>
                </>
                :
                <>
                    <TableCell component="th" scope="row">{props.diagram.name}</TableCell>
                    <TableCell align="right">{props.diagram.businessKey}</TableCell>
                    <TableCell align="right">{props.diagram.xmlFile}</TableCell>
                    <TableCell align="right">{props.diagram.comment}</TableCell>
                </>
            }
            <TableCell component="th" scope="row">
                {editMode ?
                    <IconButton onClick={() => {
                        setEditMode(false)
                        handleUpdate()
                    }} >
                        <CheckIcon color="secondary"/>
                    </IconButton>
                    :
                    <IconButton onClick={() => setEditMode(true)}><EditIcon color="primary" /></IconButton>
                }
                <IconButton onClick={() => handleDuplicate(props.diagram)}><ContentCopyIcon color="secondary" /></IconButton>
                <IconButton onClick={() => handleDelete(props.diagram.id)}><DeleteForeverIcon color="error" /></IconButton>
            </TableCell>
        </TableRow>
        </>
    );
}