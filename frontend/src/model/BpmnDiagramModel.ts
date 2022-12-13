import {CommentModel} from "./CommentModel";

export type BpmnDiagramModel = {
    id: string
    name: string
    businessKey: string
    filename: string
    version: number
    comments: CommentModel[]
    calledDiagrams: {
        id: string,
        calledFromActivityIds: string[]
    }[]
    template: boolean
    customDiagram: boolean
}
