/**
 *  @file       FreakyDroneMultiple.scd
 *
 *              Multiple instances of `FreakyDrone` sound more freaky when they
 *              are all slightly detuned from one another.
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/
Instr("FreakyDroneMultiple", {
  arg baseFreq,
    baseModFreq,
    numHarms;

  var out = Silence.ar(),
    harmNum,
    freq,
    modFreq;

  (numHarms.asInteger()).do({
    arg i;
    
    harmNum = i + 2;
    freq = baseFreq * harmNum + Rand(0.0, 10.0);
    modFreq = baseModFreq * harmNum + Rand(0.0, 10.0);

    out = out + Instr.ar("FreakyDrone", (
      baseFreq: freq,
      baseModFreq: modFreq
    ));
  
  });

  out;

}, [
  \freq,
  \freq,
  [1, 100]
]);
