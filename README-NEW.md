## Connecting to Temi
Wifi: CDI-304
Websocket Server: ws://192.168.123.10:8175


#### Speaking
```json
{
  "command": "speak",
  "sentence": "The sentence you want Temi to say",
  "id": "The id of this message"
}
```
Upon receiving the command, Temi will immediately echo the sentence back to your client, and begin to speak. You can use this to check if the sentences are in order. When Temi finishes speaking, another message will be sent to you in the format of `TTS_COMPLETED/The sentence you want Temi to say`. This will let you know when to proceed to the next action.

#### Asking a question
```json
{
  "command": "ask",
  "sentence": "The question you want Temi to ask",
  "id": "The id of this message"
}
```
This is similar to speaking, except Temi will start to recognize user speech saying the question. When the user finishes answering and the recognition is completed, you will get an message back in the form of `ASR_COMPLETED/The user's answer to the question`.

#### Going to a location
```json
{
  "command": "goto",
  "location": "The exact name of the location as set in Temi",
  "id": "The id of this message"
}
```
This will tell Temi to go to the location you specified. The string here must match the location name stored in Temi exactly for it to work.

### beWithMe (detecting human)
```json
{
  "command": "beWithMe",
  "id": "The id of this message"
}
```


...please check the rest of the commands in the code