import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import RdServices from 'react-native-rd-services';

interface ResponsePojo {
  status: string;
  message: string;
   
}

export default function App() {
  const [result, setResult] = React.useState<ResponsePojo | undefined>();

  React.useEffect(() => {
    RdServices.getFingerPrint("Mantra").then(setResult);
  }, []);

  return (
    <View style={styles.container}>
      <Text>status: {result.status}</Text>
      <Text>message: {result.message}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
