#### Speaking
```json
{
  "command": "speak",
  "sentence": "The sentence you want Temi to say"
}
```
Upon receiving the command, Temi will immediately echo the sentence back to your client, and begin to speak. You can use this to check if the sentences are in order. When Temi finishes speaking, another message will be sent to you in the format of `TTS_COMPLETED/The sentence you want Temi to say`. This will let you know when to proceed to the next action.

#### Asking a question
```json
{
  "command": "ask",
  "sentence": "The question you want Temi to ask"
}
```
This is similar to speaking, except Temi will start to recognize user speech saying the question. When the user finishes answering and the recognition is completed, you will get an message back in the form of `ASR_COMPLETED/The user's answer to the question`.

#### Going to a location
```json
{
  "command": "goto",
  "location": "The exact name of the location as set in Temi"
}
```
This will tell Temi to go to the location you specified. The string here must match the location name stored in Temi exactly for it to work.
