import React, {FormEvent, useState} from 'react';
import './css/App.css';
import DiagramTable from "./DiagramTable";
import axios from "axios";

function App() {
    const [name,setName] = useState<string>("")
    const [businessKey,setBusinessKey] = useState<string>("")
    const [xmlFile,setXmlFile] = useState<string>("")
    const [comment,setComment] = useState<string>("")

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        axios.post("/api/bpmndiagrams",{
                    "name": name,
                    "businessKey": businessKey,
                    "xmlFile": xmlFile,
                    "comment": comment
                })
            .then(() => {
                setName("")
                setBusinessKey("")
                setXmlFile("")
                setComment("")
                alert("BPMN-Diagram erfolreich hinzugefügt!")
            })
            .catch(error => console.error(error))

    }

  return (
    <>
        <h1>BPMN-Library</h1>
        <form onSubmit={handleSubmit}>
            <input placeholder="Name" value={name} onChange={e => setName(e.target.value)}/>
            <input placeholder="Businesskey" value={businessKey} onChange={e => setBusinessKey(e.target.value)}/>
            <input placeholder="XML-Filename" value={xmlFile} onChange={e => setXmlFile(e.target.value)}/>
            <input placeholder="Kommentar" value={comment} onChange={e => setComment(e.target.value)}/>
            <button type="submit">Add</button>
        </form>
        <main>
            <DiagramTable/>
        </main>
    </>
  );
}

export default App;
