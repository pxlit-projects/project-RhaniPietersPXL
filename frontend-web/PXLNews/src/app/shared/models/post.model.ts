export class Post {
    id?: number;
    title: string;
    content: string;
    author: string;
    creationDate: string;
    category: string;
    state: string;
    rejectedMessage: string;
    commentCount?: number

    constructor(title: string, content: string, author: string, creationDate: string, category: string, state: string, rejectedMessage: string) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.creationDate = creationDate;
        this.category = category;
        this.state = state;
        this.rejectedMessage = rejectedMessage;
    }
}


