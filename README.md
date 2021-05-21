# react-native-rd-services

Reading finger print data using RD services in Android.

RD service supported device :

1. Morpho
2. Mantra
3. PB510
4. SecuGen
5. ACPL FM220
6. Aratek A600

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
        const { status, message } = result;
        console.log('Status', status);
        console.log('Status', message);
    }
);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
