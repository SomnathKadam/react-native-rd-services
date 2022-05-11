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

// ... DeviceType may like package name of RD service example : 
// com.precision.pb510.rdservice, com.scl.rdservice, com.scl.rdservice, com.secugen.rdservice and so on



  let pidOption =
            "<?xml version='1.0'?><PidOptions ver='1.0'><Opts fCount='1' fType='0' iCount='0' pCount='0' format='0' pidVer='2.0' timeout='10000' posh='UNKNOWN' env='P' /><CustOpts></CustOpts></PidOptions>";



RdServices.getFingerPrint(DeviceType, pidOption).then( (result)
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
