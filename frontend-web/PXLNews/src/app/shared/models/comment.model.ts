export class Comment {
    id?: number;
    title: string;
    content: string;
    author: string;

    constructor(title: string, content: string, author: string) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}


