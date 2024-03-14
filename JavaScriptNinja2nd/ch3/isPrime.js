function isPrime(value) {
  if (!isPrime.answers) {
    isPrime.answers = {};
  } // create cache

  if (isPrime.answers[value]) {
    return isPrime.answers[value];
  }

  const prime = value > 1;
  for (let i = 2; i < value; i++) {
    if (value % i === 0) {
      prime = false;
      break;
    }
  }
  return (isPrime.answers[value] = prime);
}

console.log(isPrime(5));
console.log(isPrime(5));
