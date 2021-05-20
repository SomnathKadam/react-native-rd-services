# react-native-rd-services

Reading finger print data using RD services

## Installation

```sh
npm install react-native-rd-services
```

or using yarn

```sh
yarn add react-native-rd-services
```

## Usage

```js
import RdServices from "react-native-rd-services";

// ...

RdServices.getFingerPrint("Mantra").then( (result)
    {
        console.log('result', result);
        const { status, message } = JSON.parse(result);
        console.log('Status', status);
        console.log('Status', message);

        
    }
);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
